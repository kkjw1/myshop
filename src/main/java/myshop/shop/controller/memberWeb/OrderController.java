package myshop.shop.controller.memberWeb;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import myshop.shop.controller.HomeController;
import myshop.shop.controller.HomeController.DirectOrderDto;
import myshop.shop.dto.order.AddOrderItemDto;
import myshop.shop.dto.address.ManageAddressDto;
import myshop.shop.dto.cart.ManageCartDto;
import myshop.shop.dto.member.LoginCheckMemberDto;
import myshop.shop.dto.order.AddOrderDto;
import myshop.shop.dto.order.DetailOrderDto;
import myshop.shop.entity.order.Order;
import myshop.shop.service.AddressService;
import myshop.shop.service.CartService;
import myshop.shop.service.ItemService;
import myshop.shop.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static myshop.shop.controller.memberWeb.MemberController.SessionConst.LOGIN_MEMBER;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final AddressService addressService;
    private final CartService cartService;
    private final OrderService orderService;
    private final ItemService itemService;



    /**
     * 주문/결제 폼
     * 장바구니 폼 -> 구매하기
     */
    @GetMapping("/myPage/order")
    public String orderForm(@ModelAttribute CartToOrderDto cartToOrderDto, HttpServletRequest request, Model model) {
        log.info("cartToOrderDto={}", cartToOrderDto);
        new LoginCheckMemberDto().loginCheck(request, model);
        LoginCheckMemberDto loginCheckMemberDto = (LoginCheckMemberDto) request.getSession().getAttribute(LOGIN_MEMBER);
        Long memberNo = loginCheckMemberDto.getNo();

        // 구매 상품 재고 선점
        itemService.itemStockUpdate(cartToOrderDto.getCartNo());

        // 배송지
        ManageAddressDto manageAddressDto = addressService.getAddressByMemberNo(memberNo);

        List<ManageCartDto> manageCartDtoList = new ArrayList<>();
        // Cart내용
        for (Long cartNo : cartToOrderDto.getCartNo()) {
            manageCartDtoList.add(cartService.findCart(cartNo));
        }


        model.addAttribute("totalProductPrice", cartToOrderDto.getTotalProductPrice());
        model.addAttribute("deliveryFee", cartToOrderDto.getDeliveryFee());
        model.addAttribute("totalOrderPrice", cartToOrderDto.getTotalOrderPrice());
        model.addAttribute("manageAddressDto", manageAddressDto);
        model.addAttribute("manageCartDtoList", manageCartDtoList);

        return "member/mypage/order";
    }

    @Getter @Setter
    @ToString(of = {"cartNo", "totalProductPrice", "deliveryFee", "totalOrderPrice"})
    public static class CartToOrderDto {
        private List<Long> cartNo = new ArrayList<>();
        private int totalProductPrice;
        private int deliveryFee;
        private int totalOrderPrice;

        public CartToOrderDto() {
        }
    }



    /**
     * 주문/결제 폼
     * 상품 상세 폼 -> 바로 구매
     */
    @GetMapping("/myPage/directOrder")
    public String orderForm2(@ModelAttribute DirectOrderDto directOrderDto, HttpServletRequest request, Model model) {
        new LoginCheckMemberDto().loginCheck(request, model);

        LoginCheckMemberDto loginCheckMemberDto = (LoginCheckMemberDto) request.getSession().getAttribute(LOGIN_MEMBER);
        Long memberNo = loginCheckMemberDto.getNo();
        directOrderDto.setMemberNo(memberNo);
        log.info("주문/결제 폼, directOrderDto={}", directOrderDto);

        // 배송지
        ManageAddressDto manageAddressDto = addressService.getAddressByMemberNo(memberNo);
        ManageCartDto manageCartDto = cartService.findCart(directOrderDto);
        log.info("mangeCartDto={}", manageCartDto);



        List<ManageCartDto> manageCartDtoList = new ArrayList<>();
        manageCartDtoList.add(manageCartDto);
        int totalProductPrice = (manageCartDto.getOriginPrice() + manageCartDto.getOptionPrice()) * manageCartDto.getCount();
        int deliveryFee = totalProductPrice >= 30000 ? 0 : 3000;
        model.addAttribute("totalProductPrice", totalProductPrice);
        model.addAttribute("deliveryFee", deliveryFee);
        model.addAttribute("totalOrderPrice", totalProductPrice + deliveryFee);
        model.addAttribute("manageAddressDto", manageAddressDto);
        model.addAttribute("manageCartDtoList", manageCartDtoList);

        return "member/mypage/order";
    }



    /**
     * 주문/결제 폼 -> 배송지 변경
     */
    @GetMapping("/myPage/order/changeAddress")
    @ResponseBody
    public List<ManageAddressDto> changeAddress(HttpServletRequest request) {
        LoginCheckMemberDto loginCheckMemberDto = (LoginCheckMemberDto) request.getSession().getAttribute(LOGIN_MEMBER);
        Long memberNo = loginCheckMemberDto.getNo();

        return addressService.getAddressesByMemberNo(memberNo);
    }



    /**
     * 주문/결제 폼 -> 결제하기
     */
    @PostMapping("/myPage/order/payment")
    @ResponseBody
    public Long payment(@RequestBody AddOrderDto addOrderDto, HttpServletRequest request) {
        log.info("addOrderDto={}", addOrderDto);
        LoginCheckMemberDto loginCheckMemberDto = (LoginCheckMemberDto) request.getSession().getAttribute(LOGIN_MEMBER);
        Long memberNo = loginCheckMemberDto.getNo();

        // 주문, 배송, 상품 주문 추가
        Order order = orderService.saveOrder(memberNo, addOrderDto);

        // 장바구니에서 제거하기
        List<AddOrderItemDto> addOrderItemDtoList = addOrderDto.getAddOrderItemDtoList();
        for (AddOrderItemDto addOrderItemDto : addOrderItemDtoList) {
            cartService.removeCart(addOrderItemDto.getCartNo());
        }
        return order.getNo();
    }



    /**
     * 주문 상세 화면
     * 결제하기
     * 주문 목록 폼 -> 주문 상세 보기
     */
    @GetMapping("/order/complete")
    public String orderComplete(@RequestParam("orderNo") Long orderNo, HttpServletRequest request, Model model) {
        log.info("orderComplete, orderNo={}", orderNo);

        DetailOrderDto detailOrderDto = orderService.getOrder(orderNo);

        model.addAttribute("detailOrderDto", detailOrderDto);
        return "member/mypage/order_complete";
    }



    /**
     * 주문 내역 폼
     */
    @GetMapping("/myPage/orderList")
    public String orderListForm(HttpServletRequest request, Model model) {
        new LoginCheckMemberDto().loginCheck(request, model);
        return "member/mypage/order_list";
    }


}

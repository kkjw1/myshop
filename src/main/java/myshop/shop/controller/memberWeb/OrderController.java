package myshop.shop.controller.memberWeb;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import myshop.shop.dto.address.ManageAddressDto;
import myshop.shop.dto.cart.ManageCartDto;
import myshop.shop.dto.member.LoginCheckMemberDto;
import myshop.shop.service.AddressService;
import myshop.shop.service.CartService;
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




    /**
     * 주문/결제 폼
     */
    @GetMapping("/myPage/order")
    public String orderForm(@ModelAttribute CartToOrderDto cartToOrderDto, HttpServletRequest request, Model model) {
        log.info("cartToOrderDto={}", cartToOrderDto);
        new LoginCheckMemberDto().loginCheck(request, model);
        LoginCheckMemberDto loginCheckMemberDto = (LoginCheckMemberDto) request.getSession().getAttribute(LOGIN_MEMBER);
        Long memberNo = loginCheckMemberDto.getNo();

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
     * 주문/결제 폼 -> 배송지 변경
     */
    @GetMapping("/myPage/order/changeAddress")
    @ResponseBody
    public List<ManageAddressDto> changeAddress(HttpServletRequest request, Model model) {
        LoginCheckMemberDto loginCheckMemberDto = (LoginCheckMemberDto) request.getSession().getAttribute(LOGIN_MEMBER);
        Long memberNo = loginCheckMemberDto.getNo();

        return addressService.getAddressesByMemberNo(memberNo);
    }
}

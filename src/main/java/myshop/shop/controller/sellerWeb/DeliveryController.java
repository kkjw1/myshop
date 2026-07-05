package myshop.shop.controller.sellerWeb;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myshop.shop.controller.memberWeb.MemberController;
import myshop.shop.dto.delivery.OrderDeliveryDto;
import myshop.shop.dto.seller.LoginCheckSellerDto;
import myshop.shop.service.DeliveryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static myshop.shop.controller.memberWeb.MemberController.SessionConst.LOGIN_SELLER;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/seller/order_delivery")
    public String order_delivery(HttpServletRequest request, Model model) {
        LoginCheckSellerDto loginCheckSellerDto = (LoginCheckSellerDto) request.getSession().getAttribute(LOGIN_SELLER);
        Long sellerNo = loginCheckSellerDto.getNo();

        List<OrderDeliveryDto> orderDeliveryDtoList = deliveryService.findAllDelivery(sellerNo);

        log.info("orderDeliveryDtoList={}", orderDeliveryDtoList);
        model.addAttribute("orderDeliveryDtoList", orderDeliveryDtoList);
        return "seller/delivery/order_delivery";
    }
}

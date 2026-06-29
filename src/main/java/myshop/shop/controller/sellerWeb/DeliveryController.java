package myshop.shop.controller.sellerWeb;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DeliveryController {

    @GetMapping("/seller/order_delivery")
    public String order_delivery() {
        return "seller/delivery/order_delivery";
    }
}

package myshop.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SellerController {

    //=====판매자 화면 연결(임시)시작 =====//
    @GetMapping("/seller")
    public String sellerHomeForm() {
        return "seller/home";
    }

    @GetMapping("/item_manage")
    public String item_manage() {
        return "seller/item/item_manage";
    }


    @GetMapping("/order_delivery")
    public String order_delivery() {
        return "seller/delivery/order_delivery";
    }

    @GetMapping("/inquiry_manage")
    public String inquiry_manage() {
        return "seller/inquiry/inquiry_manage";
    }

    @GetMapping("/item_new")
    public String item_new() {
        return "seller/item/item_new";
    }


    @GetMapping("/inquiry_reply")
    public String inquiry_reply() {
        return "seller/inquiry/inquiry_reply";
    }

    @GetMapping("/order_delivery_detail")
    public String order_delivery_detail() {
        return "seller/delivery/order_delivery_detail";
    }

}

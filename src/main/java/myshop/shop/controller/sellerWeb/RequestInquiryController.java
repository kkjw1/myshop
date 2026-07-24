package myshop.shop.controller.sellerWeb;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class RequestInquiryController {


    /**
     * 고객 문의 관리 폼
     */
    @GetMapping("/seller/request_inquiry_manage")
    public String requestInquiryForm() {
        // todo: 고객 문의(취소, 반품 요청) 기능 추가하기

        return "seller/inquiry/inquiry_manage";
    }




    @GetMapping("/seller/inquiry_reply")
    public String inquiry_reply() {
        return "seller/inquiry/inquiry_reply";
    }
}

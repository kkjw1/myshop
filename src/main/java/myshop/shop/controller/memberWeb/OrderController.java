package myshop.shop.controller.memberWeb;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myshop.shop.dto.member.LoginCheckMemberDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {


    /**
     * 주문/결제 폼
     */
    @GetMapping("/myPage/order")
    public String orderForm(@RequestParam("cartNo") List<Long> cartNoList, HttpServletRequest request, Model model) {
        log.info("cartNoList={}", cartNoList);
        new LoginCheckMemberDto().loginCheck(request, model);
        return "member/mypage/order";
    }
}

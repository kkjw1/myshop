package myshop.shop.controller;

import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletRequest;
import myshop.shop.dto.member.LoginCheckMemberDto;
import myshop.shop.dto.member.LoginMemberDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    @GetMapping({"/", "/home"})
    public String home(HttpServletRequest request, Model model) {
        new LoginCheckMemberDto().loginCheck(request, model);
        return "shop/home";
    }


    /**
     * ExceptionController 테스트
     */
    @GetMapping("/testException")
    @ResponseBody
    public void test() {
        throw new EntityExistsException();
    }
}

package myshop.shop.controller;

import jakarta.servlet.http.HttpServletRequest;
import myshop.shop.dto.member.LoginCheckMemberDto;
import myshop.shop.dto.member.LoginMemberDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping({"/", "/home"})
    public String home(HttpServletRequest request, Model model) {
        new LoginCheckMemberDto().loginCheck(request, model);
        return "shop/home";
    }
}

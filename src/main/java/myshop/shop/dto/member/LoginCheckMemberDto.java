package myshop.shop.dto.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import myshop.shop.controller.MemberController;
import org.springframework.ui.Model;

import static myshop.shop.controller.MemberController.SessionConst.LOGIN_MEMBER;

@Getter
public class LoginCheckMemberDto {

    private String id;
    private String name;

    public LoginCheckMemberDto() {
    }

    public LoginCheckMemberDto(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean loginCheck(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            model.addAttribute("isLogin", false);
            return false;
        }

        LoginCheckMemberDto loginMember = (LoginCheckMemberDto) session.getAttribute(LOGIN_MEMBER);

        if (loginMember == null) {
            model.addAttribute("isLogin", false);
            return false;
        }


        model.addAttribute("loginMember", loginMember);
        model.addAttribute("isLogin", true);
        return true;
    }
}

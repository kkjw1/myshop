package myshop.shop.dto.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import myshop.shop.controller.MemberController;
import org.springframework.ui.Model;

import static myshop.shop.controller.MemberController.SessionConst.LOGIN_MEMBER;

@Data
@ToString(of = {"id", "password"})
public class LoginMemberDto {
    @NotBlank(message = "아이디가 공백입니다.")
    String id;
    @NotBlank(message = "비밀번호가 공백입니다.")
    String password;

    public LoginMemberDto() {
    }

    public LoginMemberDto(String id) {
        this.id = id;
    }

    public LoginMemberDto(String id, String password) {
        this.id = id;
        this.password = password;
    }
}

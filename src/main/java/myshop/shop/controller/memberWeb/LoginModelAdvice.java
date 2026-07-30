package myshop.shop.controller.memberWeb;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class LoginModelAdvice {

    // @ControllerAdvice의 @ModelAttribute를 쓰면 필터가 인증만 해두고, Model에 값 넣는 건 컨트롤러마다
    // 반복 호출 안 해도 전역으로 자동 처리할 수 있습니다.
    // 지금 loginCheck()가 하는 일을 그대로 옮기는 방식이에요.

    @ModelAttribute
    public void addLoginInfo(Model model) {

        // todo: myPage접근시 로그인 확인하는 것 까지 제작함, model에 isLogin과 loginCheckMemberDto보내는거 추가하기,
        // 클로드의 JWT 토큰 검증 필터 구현의 [ 1. Principal에 필요한 정보(id, name) 담기 ] 부터하면 됨

/*        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isLogin = authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof LoginMemberPrincipal;

        model.addAttribute("isLogin", isLogin);

        if (isLogin) {
            model.addAttribute("loginMember", authentication.getPrincipal());
        }*/
    }
}

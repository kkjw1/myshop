package myshop.shop.controller.memberWeb;

import lombok.extern.slf4j.Slf4j;
import myshop.shop.controller.HomeController;
import myshop.shop.dto.member.LoginCheckMemberDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@ControllerAdvice(basePackages = "myshop.shop.controller.memberWeb", assignableTypes = {HomeController.class})
public class LoginModelAdvice {

    // @ControllerAdvice의 @ModelAttribute를 쓰면 필터가 인증만 해두고, Model에 값 넣는 건 컨트롤러마다
    // 반복 호출 안 해도 전역으로 자동 처리할 수 있습니다.
    // 지금 loginCheck()가 하는 일을 그대로 옮기는 방식이에요.

    @ModelAttribute
    public void addLoginInfo(Model model) {
        // 여기서 SecurityContextHolder.getContext().getAuthentication();를 사용해서 로그인 관련 처리하기 + 로그인을 했을 때도
        // SecurityContextHolder.getContext().getAuthentication(); 에 저장하는거 넣기
        // todo: myPage접근시 로그인 확인하는 것 까지 제작함, model에 isLogin과 loginCheckMemberDto보내는거 추가하기,
        // 클로드의 JWT 토큰 검증 필터 구현의 [ 1. Principal에 필요한 정보(id, name) 담기 ] 부터하면 됨
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isLogin = authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof LoginCheckMemberDto;

        model.addAttribute("isLogin", isLogin);

        if (authentication.getPrincipal() instanceof LoginCheckMemberDto loginCheckMemberDto) {
            model.addAttribute("loginCheckMemberDto", loginCheckMemberDto);
        }



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

package myshop.shop.interceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import myshop.shop.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import static myshop.shop.controller.memberWeb.MemberController.SessionConst.LOGIN_MEMBER;

public class LoginCheckMemberInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    public LoginCheckMemberInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //@Controller @RequestMapping
        if (handler instanceof HandlerMethod) {

            Cookie[] cookies = request.getCookies();
            String token = null;
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }

            try {
                jwtService.parseClaims(token);
                return true;

            } catch (JwtException e) {          // 토큰 만료,위조
                String uri = request.getRequestURI();
                String queryString = request.getQueryString();
                String redirectURL = (queryString != null) ? uri + "?" + queryString : uri;
                response.sendRedirect("/login?redirectURL=" + redirectURL);
                return false;
            }
        }
        //정적 리소스
        return true;
    }
}

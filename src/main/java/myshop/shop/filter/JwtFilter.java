package myshop.shop.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myshop.shop.service.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String token = getToken(request, "accessToken");

        if (token != null) {
            try {
                Claims claims = jwtService.parseClaims(token);

                String memberId = claims.getSubject();
                String memberName = claims.get("name", String.class);

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        memberId, null, List.of(new SimpleGrantedAuthority("NAME_" + memberName))
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("accessToken 인증 완료, memberId={}", memberId);

            } catch (ExpiredJwtException e) {
                String refreshToken = getToken(request, "refreshToken");

                if (refreshToken != null && jwtService.validToken(refreshToken)) {
                    // Refresh Token이 유효하다면 즉시 새 Access Token 발급!
                    Claims refreshClaims = jwtService.parseClaims(refreshToken);
                    String memberId = refreshClaims.getSubject();
                    String memberName = refreshClaims.get("name", String.class);


                    String newAccessToken = jwtService.createAccessToken(memberId, memberName);

                    ResponseCookie newAccessCookie = ResponseCookie
                            .from("accessToken", newAccessToken)
                            .httpOnly(true)
                            .secure(false)
                            .path("/")
                            .maxAge(Duration.ofMinutes(30))
                            .sameSite("Strict")
                            .build();

                    response.addHeader(HttpHeaders.SET_COOKIE, newAccessCookie.toString());

                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            memberId, null, List.of(new SimpleGrantedAuthority("NAME_" + memberName))
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("accessToken 재발급 완료, memberId={}", memberId);
                } else {
                    log.info("accessToken 만료, refreshToken 만료");
                    response.sendRedirect("/");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * 쿠키에서 token 가져옴
     */
    private String getToken(HttpServletRequest request, String tokenType) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (tokenType.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}

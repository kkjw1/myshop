package myshop.shop.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import myshop.shop.dto.member.LoginCheckMemberDto;
import myshop.shop.entity.member.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    String secret;

    private SecretKey secretKey;

    private final long accessTokenValidity = 1000 * 60 * 30; // 30분
    private final long refreshTokenValidity = 1000L * 60 * 60 * 24 * 14; // 14일
    private final RedisService redisService;


    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }



    /**
     * AccessToken 발급
     * 로그인 화면 -> 로그인
     * 홈페이지 접속 -> RefreshToken으로 재발급
     */
    public String createAccessToken(Member member) {
        return Jwts.builder()
                .subject(member.getId())
                .claim("no", member.getNo())
                .claim("id", member.getId())
                .claim("name", member.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(secretKey)
                .compact();
    }
    public String createAccessToken(LoginCheckMemberDto loginCheckMemberDto) {
        return Jwts.builder()
                .subject(loginCheckMemberDto.getId())
                .claim("no", loginCheckMemberDto.getNo())
                .claim("id", loginCheckMemberDto.getId())
                .claim("name", loginCheckMemberDto.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(secretKey)
                .compact();
    }


    /**
     * RefreshToken 발급
     * 로그인 화면 -> 로그인
     */
    public String createRefreshToken(Member member, long validityMillis) {
        return Jwts.builder()
                .subject(member.getId())
                .claim("no", member.getNo())
                .claim("id", member.getId())
                .claim("name", member.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + validityMillis))
                .signWith(secretKey)
                .compact();
    }


    /**
     * 토큰 데이터 불러오기 (토큰 파싱)
     */
    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    /**
     * refresh 토큰 검증
     */
    public boolean validToken(String clientToken) {
        if (!StringUtils.hasText(clientToken)) {
            return false;
        }
        try {
            Claims claims = parseClaims(clientToken);

            String memberId = claims.getSubject();
            String serverToken = redisService.getToken(memberId);

            if (StringUtils.hasText(serverToken) && clientToken.equals(serverToken)) {
                return true;
            }
            return false;

        } catch (JwtException e) {      // 위조, 만료
            return false;
        }
    }

    public LoginCheckMemberDto getMember(String token) {
        Claims claims = parseClaims(token);
        Number noNumber = claims.get("no", Number.class);
        Long no = (noNumber != null) ? noNumber.longValue() : null;
        return new LoginCheckMemberDto(no,
                claims.get("id", String.class),
                claims.get("name", String.class));
    }
}

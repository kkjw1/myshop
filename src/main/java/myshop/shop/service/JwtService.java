package myshop.shop.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    String secret;

    private SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

    private final long accessTokenValidity = 1000 * 60 * 30; // 30분
    private final long refreshTokenValidity = 1000L * 60 * 60 * 24 * 14; // 14일
    private final RedisService redisService;



    /**
     * 토큰 삭제
     */

    public String createAccessToken(String memberId, String memberName) {
        return Jwts.builder()
                .subject(memberId)
                .claim("name", memberName)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(secretKey)
                .compact();
    }


    public String createRefreshToken(String memberId, long validityMillis) {
        return Jwts.builder()
                .subject(memberId)
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

    public String getMemberId(String token) {
        return String.valueOf(parseClaims(token).getSubject());
    }

    public String getMemberName(String token) {
        return parseClaims(token).get("name", String.class);
    }

}

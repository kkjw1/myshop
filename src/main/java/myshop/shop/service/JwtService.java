package myshop.shop.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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

    private final StringRedisTemplate stringRedisTemplate;
    private final long accessTokenValidity = 1000 * 60 * 30; // 30분
    private final long refreshTokenValidity = 1000L * 60 * 60 * 24 * 14; // 14일

    private static final String KEY_PREFIX = "refresh:";



    /**
     * 토큰 저장
     */
    public void save(Long memberNo, String refreshToken, long validityMillis) {
        stringRedisTemplate.opsForValue().set(
                KEY_PREFIX + memberNo,
                refreshToken,
                Duration.ofMillis(validityMillis)
        );
    }


    /**
     * 토큰 삭제
     */
    public void delete(Long memberNo) {
        stringRedisTemplate.delete(KEY_PREFIX + memberNo);
    }


    public String createAccessToken(Long memberNo, String id) {
        return Jwts.builder()
                .subject(String.valueOf(memberNo))
                .claim("id", id)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(secretKey)
                .compact();
    }


    public String createRefreshToken(Long memberId, long validityMillis) {
        return Jwts.builder()
                .subject(String.valueOf(memberId))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + validityMillis))
                .signWith(secretKey)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw e; // 만료는 재발급 로직에서 따로 처리하도록 구분
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


    public Optional<String> findBymemberNo(Long memberNo) {
        return Optional.ofNullable(stringRedisTemplate.opsForValue().get(KEY_PREFIX + memberNo));
    }
}

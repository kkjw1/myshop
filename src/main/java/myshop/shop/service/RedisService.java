package myshop.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveData(String key, String value, Long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.MINUTES);
        log.info("Redis 저장 ({}:{})", key, value);
    }

    public String getData(String key) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        return (String) valueOperations.get(key);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }
}

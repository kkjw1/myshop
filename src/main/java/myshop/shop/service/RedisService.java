package myshop.shop.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisMessageListenerContainer listenerContainer;
    private final ItemService itemService;

    public static final String RESERVE_KEY = "reserve:";

    // TTL 설정
    @PostConstruct
    public void init() {
        listenerContainer.addMessageListener(this,
                new PatternTopic("__keyevent@0__:expired"));
        log.info("Redis keyspace Notification 리스너 등록");
    }

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

    //reserve:5:cart:[1, 2, 3]
    //reserve:5:direct:3:null:1
    @Override
    public void onMessage(Message message, byte @Nullable [] pattern) {
        String expiredKey = new String(message.getBody());
        log.info("Redis 키 만료 감지, {}", expiredKey);

        if (expiredKey.startsWith(RESERVE_KEY))     {
            String[] keySplit = expiredKey.split(":");
            if (keySplit[2].equals("direct")) {
                Long itemNo = Long.valueOf(keySplit[3]);
                Long optionNo = keySplit[4].equals("null") ? null : Long.valueOf(keySplit[4]);
                int count = Integer.parseInt(keySplit[5]);

                itemService.directRollbackStock(itemNo, optionNo, count);
            }
            if (keySplit[2].equals("cart")) {
                String cartStr = keySplit[3]; // "[1, 2, 3]"
                String cleanStr = cartStr.replaceAll("[\\[\\]\\s]", "");
                List<Long> cartList = Arrays.stream(cleanStr.split(","))
                        .map(Long::valueOf)
                        .toList();

                itemService.cartRollbackStock(cartList);
            }
        }
    }
}

package myshop.shop.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.Filter;
import myshop.shop.filter.LogbackFilter;
import myshop.shop.interceptor.LoginCheckMemberInterceptor;
import myshop.shop.interceptor.LoginCheckSellerInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Optional;
import java.util.UUID;

@Configuration
public class MyShopConfig implements WebMvcConfigurer {

    /**
     * redis 설정
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory("localhost", 6379);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // Java 객체를 Redis에 저장할 때 직렬화(Serialization) 방식을 설정합니다.
        // 아래 설정을 해야 Redis Desktop Manager 등에서 데이터를 읽기 편합니다.
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }


    /**
     * 등록자, 수정자
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of(UUID.randomUUID().toString());
    }


    /**
     * Querydsl
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }


    /**
     * 필터 설정
     */
    @Bean
    public FilterRegistrationBean LogFilter() {
        FilterRegistrationBean<Filter> filter = new FilterRegistrationBean<>();
        filter.setFilter(new LogbackFilter());
        filter.setOrder(1);
        filter.addUrlPatterns("/*");
        return filter;
    }

    /**
     * 인터셉터 설정
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckMemberInterceptor())
                .order(1)
                .addPathPatterns("/myPage/**");

        registry.addInterceptor(new LoginCheckSellerInterceptor())
                .order(2)
                .addPathPatterns("/seller/**")
                .excludePathPatterns("/seller/login");
    }
}

package myshop.shop.config;

import com.solapi.sdk.message.service.DefaultMessageService;
import jakarta.servlet.http.HttpServletRequest;
import myshop.shop.controller.MemberController;
import myshop.shop.dto.member.LoginCheckMemberDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;
import java.util.UUID;

import static myshop.shop.controller.MemberController.SessionConst.LOGIN_MEMBER;

@Configuration
public class MyShopConfig {

/*
    @Value("${solapi.api-key}")
    private String apiKey;
    @Value("${solapi.api-secret}")
    private String apiSecret;

    */
/**
     * SOLAPI 메시지 인증
     *//*

    @Bean
    public DefaultMessageService messageService() {
        return new DefaultMessageService(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

*/

    /**
     * 등록자, 수정자
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of(UUID.randomUUID().toString());
    }



}

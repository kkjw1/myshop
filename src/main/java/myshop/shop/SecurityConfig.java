package myshop.shop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 암호화, 복호화
     * 구현체: BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/**").permitAll()  // 해당하는 페이지들을 모두 허용한다.
                        // 관리자=선생님(ROLE_ADMIN)만 접근 허용
                        // "/api/v1/auth/admin/**" 라고 설정해줄 수도 있음.
                        //.requestMatchers("/admin/**").hasRole("ADMIN") => ROLE_ 접두사가 자동으로 들어감.
                        //.requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")

                        // 로그인한 사용자(ROLE_USER)만 접근 허용
                        // "/api/v1/auth/y/**" 라고 설정해줄 수도 있음.
                        // .requestMatchers("/logout",
                        //         "/createList",
                        //         "/deleteId").hasAnyAuthority("ROLE_USER")

                        // + 최고 권한의 관리자 필요함. ( 필수는 아님. 추후 고려해보기. )
                        // 나머지 요청은 인증된 사용자에게만 접근 허용
                        .anyRequest().authenticated()       // 모든 요청 닫음
                )

                // (버전에 따라 변경된 부분) 기본 로그인 폼을 사용하지 않도록 설정
//                .formLogin((form) -> form.disable())
                // 로그아웃은 아래 코드 말고 따로 설정할거임.
//                .logout((logout) -> logout.permitAll());   // 로그아웃 허용

                //Spring Security가 로그아웃 처리를 성공적으로 마친 뒤 기본 설정에 따라 로그인 페이지로 리다이렉트시켰음을 의미합니다.
                //(Spring Security는 로그아웃 성공 시 기본적으로 /login?logout으로 보냅니다.)
                .logout(logout -> logout
                    .logoutUrl("/logout") // 로그아웃 처리 URL (기본값 /logout)
                    .logoutSuccessUrl("/") // 로그아웃 성공 후 이동할 URL
                    .invalidateHttpSession(true) // 세션 무효화
                    .deleteCookies("JSESSIONID") // 쿠키 삭제
                    .permitAll()
        );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }
}

package myshop.shop.service;

import jakarta.persistence.EntityManager;
import myshop.shop.dto.member.LoginMemberDto;
import myshop.shop.dto.member.SignUpMemberDto;
import myshop.shop.entity.Gender;
import myshop.shop.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired EntityManager em;

    @Autowired Environment env;

    @Test
    public void signUpTest() {
        SignUpMemberDto signUpMemberDto = new SignUpMemberDto("id", "password", "email", "name", "telecom", Gender.MAN, "phoneNumber");
        Member member = memberService.signUp(signUpMemberDto);

        assertThat(member.getId()).isEqualTo("id");
    }

    @Test
    @Commit
    public void loginTest() throws Exception {
        //given
        SignUpMemberDto signUpMemberDto = new SignUpMemberDto("id", "password", "email", "name", "telecom", Gender.MAN, "phoneNumber");
        memberService.signUp(signUpMemberDto);

        //when
        Member login = memberService.login(new LoginMemberDto("id", "password"));

        //then
        assertThat(login.getId()).isEqualTo("id");
    }
    
    @Test
    public void envTest() throws Exception {
        System.out.println(env.getProperty("solapi.api-key"));
        System.out.println(env.getProperty("solapi.api-secret"));
        System.out.println(env.getProperty("solapi.sender"));
    }

    @Test
    public void authTest() throws Exception {
        //given
        String authCode = memberService.smsAuth();
        System.out.println("authCode = " + authCode);

    }
}
package myshop.shop.repository.member;

import jakarta.persistence.EntityManager;
import myshop.shop.dto.member.SignUpMemberDto;
import myshop.shop.entity.Gender;
import myshop.shop.entity.Member;
import myshop.shop.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired MemberService memberService;
    @Autowired EntityManager em;


    @Test
    public void deleteByIdTest() throws Exception {
        //given
        SignUpMemberDto signUpMemberDto = new SignUpMemberDto("id", "password", "email", "name", "telecom", Gender.MAN, "phoneNumber");
        memberService.signUp(signUpMemberDto);
        em.flush();
        em.clear();

        //when
        int count = memberRepository.deleteById("id");
        Member member = memberRepository.findById("id").orElse(null);

        //then
        assertThat(count).isEqualTo(1);
        assertThat(member).isNull();
    }


}
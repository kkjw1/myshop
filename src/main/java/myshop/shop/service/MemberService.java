package myshop.shop.service;

import lombok.RequiredArgsConstructor;
import myshop.shop.dto.member.LoginMemberDto;
import myshop.shop.dto.member.SignUpMemberDto;
import myshop.shop.entity.Member;
import myshop.shop.entity.MemberLevel;
import myshop.shop.repository.member.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    public Member signUp(SignUpMemberDto signUpMemberDto) {

        String encode = passwordEncoder.encode(signUpMemberDto.getPassword());

        return memberRepository.save(new Member(signUpMemberDto.getId(), signUpMemberDto.getEmail(), encode, signUpMemberDto.getName(),
                signUpMemberDto.getTelecom(), signUpMemberDto.getPhoneNumber(), signUpMemberDto.getGender(), MemberLevel.normal));
    }

    /**
     * 회원 중복체크
     * @return 중복아니면 true, 중복이면 false
     */
    public boolean checkId(String id) {
        return memberRepository.findById(id) == null;
    }

    /**
     * 로그인
     */
    public boolean login(LoginMemberDto loginMemberDto) {
        Member member = memberRepository.findById(loginMemberDto.getId());
        if (member != null) {
            return passwordEncoder.matches(loginMemberDto.getPassword(), member.getPassword());
        }
        return false;
    }
}

package myshop.shop.service;

import lombok.RequiredArgsConstructor;
import myshop.shop.dto.member.LoginMemberDto;
import myshop.shop.dto.member.SignUpMemberDto;
import myshop.shop.entity.Member;
import myshop.shop.entity.MemberLevel;
import myshop.shop.entity.log.LoginLog;
import myshop.shop.repository.log.LoginLogRepository;
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
    private final LoginLogRepository loginLogRepository;

    /**
     * 회원가입
     */
    public Member signUp(SignUpMemberDto signUpMemberDto) {

        String passwordEncode = passwordEncoder.encode(signUpMemberDto.getPassword());

        return memberRepository.save(new Member(signUpMemberDto.getId(), signUpMemberDto.getEmail(), passwordEncode, signUpMemberDto.getName(),
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
    public Member login(LoginMemberDto loginMemberDto) {
        Member member = memberRepository.findById(loginMemberDto.getId());
        if (member != null && passwordEncoder.matches(loginMemberDto.getPassword(), member.getPassword())) {
            loginLogRepository.save(new LoginLog(loginMemberDto.getId()));
            return member;
        }
        return null;
    }
}

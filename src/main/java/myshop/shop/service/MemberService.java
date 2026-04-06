package myshop.shop.service;

import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.exception.SolapiMessageNotReceivedException;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.service.DefaultMessageService;
import lombok.RequiredArgsConstructor;
import myshop.shop.dto.member.LoginMemberDto;
import myshop.shop.dto.member.SignUpMemberDto;
import myshop.shop.entity.Member;
import myshop.shop.entity.MemberLevel;
import myshop.shop.entity.log.LoginLog;
import myshop.shop.repository.log.LoginLogRepository;
import myshop.shop.repository.member.MemberRepository;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginLogRepository loginLogRepository;
    private final Environment env;
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
        return memberRepository.findById(id).orElse(null) == null;
    }

    /**
     * 로그인
     */
    public Member login(LoginMemberDto loginMemberDto) {
        Member member = memberRepository.findById(loginMemberDto.getId()).orElse(null);
        if (member != null && passwordEncoder.matches(loginMemberDto.getPassword(), member.getPassword())) {
            loginLogRepository.save(new LoginLog(loginMemberDto.getId()));
            return member;
        }
        return null;
    }

    /**
     * 핸드폰 중복체크
     * @return 중복아니면 true, 중복이면 false
     */
    public boolean checkPhoneNumber(String phoneNumber) {
        return memberRepository.findByPhoneNumber(phoneNumber).orElse(null) == null;
    }

    /**
     * 핸드폰 인증
     */
    public String smsAuth(String phoneNumber) {
        DefaultMessageService messageService =  SolapiClient.INSTANCE.createInstance(env.getProperty("solapi.api-key"),
                env.getProperty("solapi.api-secret"));

        String authCode = authCodeCreate();

        Message message = new Message();
        message.setFrom(env.getProperty("solapi.sender"));
        message.setTo(phoneNumber);
        message.setText("[" + authCode + "] 본인확인 인증번호를 입력하세요.");

        try {
            messageService.send(message);
        } catch (SolapiMessageNotReceivedException exception) {
            System.out.println(exception.getFailedMessageList());
            System.out.println(exception.getMessage());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return authCode;
    }

    /**
     * 인증번호6자리
     */
    public static String authCodeCreate() {
        int authCode = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return String.valueOf(authCode);
    }
}

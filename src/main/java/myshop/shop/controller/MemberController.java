package myshop.shop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import myshop.shop.dto.member.*;
import myshop.shop.entity.Gender;
import myshop.shop.entity.Member;
import myshop.shop.repository.member.MemberRepository;
import myshop.shop.service.AddressService;
import myshop.shop.service.MailService;
import myshop.shop.service.MemberService;
import myshop.shop.service.RedisService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

import static org.springframework.util.StringUtils.hasText;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final AddressService addressService;
    private final RedisService redisService;
    private final MailService mailService;

    /**
     * 로그인 폼
     */
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginMemberDto", new LoginMemberDto());
        return "member/login";
    }



    /**
     * 회원가입 완료 후 로그인 폼
     * 아이디 찾기 완료 후 로그인 폼
     */
    @GetMapping("login/{loginId}")
    public String loginForm2(@PathVariable("loginId") String id, Model model) {
        model.addAttribute("loginMemberDto", new LoginMemberDto(id));
        return "member/login";
    }



    /**
     * 로그인
     */
    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("loginMemberDto") LoginMemberDto loginMemberDto, BindingResult bindingResult,
                        HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            log.info("login fail");
            return "member/login";
        }

        Member login = memberService.login(loginMemberDto);
        if (login == null) {
            bindingResult.reject("loginFail","로그인 실패 메시지");
            return "member/login";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, new LoginCheckMemberDto(login.getId(),login.getName()));
        return "redirect:/";
    }



    /**
     * 로그아웃은 security Spring에서 알아서 처리("/logout", 세션 쿠키 삭제)
     */



    /**
     * 회원가입 폼
     */
    @GetMapping("/signUp")
    public String signUpForm(Model model) {
        model.addAttribute("signUpMemberDto", new SignUpMemberDto());
        return "member/signUp";
    }



    /**
     * 회원가입 -> 아이디 중복 체크(onblur)
     */
    @GetMapping("/checkId")
    @ResponseBody
    public boolean checkDuplicateId(@RequestParam("id") String id) {
        return memberService.checkId(id);
    }



    /**
     * 회원가입
     */
    @PostMapping("/signUp")
    public String signUp(@Validated @ModelAttribute("signUpMemberDto") SignUpMemberDto signUpMemberDto, BindingResult bindingResult,
                         RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            log.info("error={}", bindingResult);
            return "member/signUp";
        }

        String data = redisService.getData("confirmedPhone:" + signUpMemberDto.getPhoneNumber());
        log.info("data={}", data);
        if (data == null) {
            log.info("핸드폰이 인증되지 않았습니다.");
            bindingResult.reject("phoneAuthFail", "핸드폰 인증 실패 메시지");
            return "member/signUp";
        }

        Member signed = memberService.signUp(signUpMemberDto);
        log.info("signUp complete, {}", signed);

        redirectAttributes.addAttribute("loginId", signUpMemberDto.getId());
        return "redirect:/login/{loginId}";
    }

    public static class SessionConst {
        public static final String LOGIN_MEMBER="loginMember";
    }



    /**
     * 회원가입 -> 핸드폰번호 중복 체크
     * 아이디,비밀번호 찾기 -> 핸드폰번호 체크
     */
    @GetMapping("/checkPhoneNumber")
    @ResponseBody
    public Boolean checkDuplicatePhoneNumber(@RequestParam("phoneNumber") String phoneNumber) {
        boolean result = memberService.checkPhoneNumber(phoneNumber);
        log.info("checkDuplicatePhoneNumber, phoneNumber={} result={}", phoneNumber, result);
        return result;
    }



    /**
     * 회원가입 -> 이메일 중복 확인(onblur)
     * 아이디,비밀번호 찾기 -> 이메일 중복 체크
     */
    @GetMapping("/checkEmail")
    @ResponseBody
    public Boolean checkDuplicateEmail(@RequestParam("email") String email) {
        boolean result = memberService.checkEmail(email);
        log.info("checkDuplicateEmail, email={} result={}", email, result);
        return result;
    }



    /**
     * 회원가입 -> 핸드폰 인증
     * 아이디,비밀번호 찾기 -> 핸드폰 인증
     */
    @PostMapping("/sendSmsAuth")
    @ResponseBody
    public String sendSmsAuth(@RequestBody SendSmsAuthDto sendSmsAuthDto) {
        log.info("phoneNumber={}", sendSmsAuthDto.getPhoneNumber());
        String authCode = memberService.smsAuth(sendSmsAuthDto.getPhoneNumber());
//        String authCode = "123456";
        redisService.saveData("sendSmsAuth:" + sendSmsAuthDto.getPhoneNumber(), authCode, 3L);
        return "ok";
    }

    @Getter @Setter
    public static class SendSmsAuthDto {
        private String phoneNumber;
    }



    /**
     * 회원가입 -> 핸드폰 인증번호 확인
     */
    @PostMapping("/checkAuthCode")
    @ResponseBody
    public String checkAuthCode(@RequestBody CheckSmsAuthCodeDto checkSmsAuthCodeDto,
                                HttpServletRequest request) {
        String authCode = checkSmsAuthCodeDto.getAuthCode();
        String phoneNumber = checkSmsAuthCodeDto.getPhoneNumber();
        log.info("authCode:{}, phoneNumber:{}", authCode, phoneNumber);

        String data = redisService.getData("sendSmsAuth:"+phoneNumber);
        boolean result = authCode.equals(data);
        log.info("authCode.equals:{}", result);
        if (result) {
            redisService.deleteData("sendSmsAuth:"+phoneNumber);
            redisService.saveData("confirmedPhone:"+phoneNumber, "SUCCESS", 10L);
            return "ok";
        }
        return "no";
    }

    @Getter @Setter
    public static class CheckSmsAuthCodeDto {
        private String authCode;
        private String phoneNumber;
    }



    /**
     * 아이디, 비밀번호 찾기 폼
     */
    @GetMapping("/findMember")
    public String findMemberForm() {
        return "member/find_member";
    }



    /**
     * 아이디,비밀번호 찾기 -> 핸드폰 인증번호 확인
     */
    @PostMapping("/findMember/checkSmsAuthCode")
    public String checkSmsAuthCode(@RequestBody CheckSmsAuthCodeDto checkSmsAuthCodeDto, RedirectAttributes redirectAttributes) {

        String authCode = checkSmsAuthCodeDto.getAuthCode();
        String phoneNumber = checkSmsAuthCodeDto.getPhoneNumber();
        log.info("checkSmsAuthCode authCode:{}, phoneNumber:{}", authCode, phoneNumber);

        String data = redisService.getData("sendSmsAuth:"+phoneNumber);
        boolean result = authCode.equals(data);
        log.info("result:{}", result);
        if (result) {
            redisService.deleteData("sendSmsAuth:"+phoneNumber);
            Member member = memberRepository.findByPhoneNumber(phoneNumber).orElse(null);
            String memberId = member.getId();
            redisService.saveData("findMemberId:"+ memberId, "SUCCESS", 10L);
            redirectAttributes.addAttribute("memberId", memberId);
            return "redirect:/resetPassword";
        }
        return "member/find_member";
    }



    /**
     * 아이디 비밀번호 찾기 -> 이메일 인증
     */
    @PostMapping("/sendEmailAuth")
    @ResponseBody
    public String sendEmailAuth(@RequestBody SendEmailAuthDto sendEmailAuthDto) {
        log.info("toAddress={}", sendEmailAuthDto.getToAddress());
        String authCode = mailService.authCodeCreate();
        mailService.sendMail(sendEmailAuthDto.getToAddress(), authCode);
        redisService.saveData("sendEmailAuth:" + sendEmailAuthDto.getToAddress(), authCode, 3L);
        return "ok";
    }

    @Getter @Setter
    public static class SendEmailAuthDto {
        private String toAddress;
    }



    /**
     * 아이디,비밀번호 찾기 -> 이메일 인증번호 확인
     */
    @PostMapping("/findMember/checkEmailAuthCode")
    public String checkEmailAuthCode(@RequestBody CheckEmailAuthCode checkEmailAuthCode, RedirectAttributes redirectAttributes) {

        String authCode = checkEmailAuthCode.getAuthCode();
        String toAddress = checkEmailAuthCode.getToAddress();
        log.info("checkEmailAuthCode authCode:{}, toAddress:{}", authCode, toAddress);

        String data = redisService.getData("sendEmailAuth:" + toAddress);
        boolean result = authCode.equals(data);
        log.info("authCode.equals:{}", result);

        if (result) {
            redisService.deleteData("sendEmailAuth:" + toAddress);
            Member member = memberRepository.findByEmail(toAddress).orElse(null);
            String memberId = member.getId();
            redisService.saveData("findMemberId:"+ memberId, "SUCCESS", 10L);
            redirectAttributes.addAttribute("memberId", memberId);
            return "redirect:/resetPassword";
        }
        return "member/find_member";
    }

    @Getter @Setter
    public static class CheckEmailAuthCode {

        private String authCode;
        private String toAddress;
    }



    /**
     * 비밀번호 재설정 폼
     */
    @GetMapping("/resetPassword")
    public String resetPasswordForm(@RequestParam("memberId") String memberId, Model model) {
        log.info("findMember:{}", memberId);

        String data = redisService.getData("findMemberId:" + memberId);

        if (!data.equals("SUCCESS")) {
            log.error("잘못된 접근입니다.");
            return "redirect:/login";
        }

        model.addAttribute("resetPasswordMemberDto", new ResetPasswordMemberDto(memberId));
        return "member/reset_password";
    }


    /**
     * 비밀번호 재설정
     */
    @PostMapping("/resetPassword")
    public String resetPassword(@Validated @ModelAttribute("resetPasswordMemberDto") ResetPasswordMemberDto resetPasswordMemberDto, BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "member/reset_password";
        }

        int result = memberService.resetPassword(resetPasswordMemberDto);
        log.info("result:{}",result);

        redirectAttributes.addAttribute("loginId", resetPasswordMemberDto.getId());
        return "redirect:/login/{loginId}";
    }



    /**
     * 개인정보 확인/수정 폼
     */
    @GetMapping("/myPage/memberModify")
    public String memberModifyForm(@RequestParam("memberId") String memberId, Model model, HttpServletRequest request,
                                   RedirectAttributes redirectAttributes) {

        String access = redisService.getData("ModifyCheckPW:" + memberId);
        if (access == null) {
            redirectAttributes.addAttribute("memberId", memberId);
            return "redirect:/memberModifyCheckPW";
        }

        if (!new LoginCheckMemberDto().loginCheck(request, model)) {
            return "redirect:/member/login?redirectURL=" + request.getRequestURI();
        }

        Member member = memberRepository.findById(memberId).orElse(null);
        UpdateMemberDto updateMember = new UpdateMemberDto(member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPassword(),
                member.getTelecom(),
                member.getPhoneNumber(),
                member.getGender());
        model.addAttribute("updateMember", updateMember);

        return "member/mypage/member_modify";
    }



    /**
     * 개인정보 확인/수정 -> 비밀번호 재확인 폼
     */
    @GetMapping("/memberModifyCheckPW")
    public String memberModifyCheckPWForm(@RequestParam("memberId") String memberId, Model model) {
        LoginMemberDto loginMemberDto = new LoginMemberDto();
        loginMemberDto.setId(memberId);
        model.addAttribute("loginMemberDto", loginMemberDto);
        return "member/mypage/member_modify_checkPW";
    }



    /**
     * 비밀번호 재확인 폼 -> 비밀번호 확인
     */
    @PostMapping("/memberModifyCheckPW")
    public String memberModifyCheckPW(@Validated @ModelAttribute LoginMemberDto loginMemberDto, BindingResult bindingResult,
                                      Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "member/mypage/member_modify_checkPW";
        }

        Member member = memberService.login(loginMemberDto);
        if (member == null) {
            log.info("memberModifyCheckPW Error");
            bindingResult.reject("CheckPWError", "비밀번호 체크 에러");
            return "member/mypage/member_modify_checkPW";
        }

        redisService.saveData("ModifyCheckPW:"+member.getId(), "SUCCESS", 10L);

        redirectAttributes.addAttribute("memberId", member.getId());
        return "redirect:/myPage/memberModify";
    }



    /**
     * 개인정보 확인/수정 -> 수정하기
     */
    @PostMapping("/myPage/memberModify")
    public String memberModify(@Validated @ModelAttribute("updateMember") UpdateMemberDto updateMemberDto, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            log.info("myPageUpdate Fail={}", bindingResult);
            return "member/mypage/member_modify";
        }

        Member beforeMember = memberRepository.findById(updateMemberDto.getId()).orElse(null);
        Member updateMember = memberService.memberModify(updateMemberDto);

        ChangedDataDto changedDataDto = new ChangedDataDto(beforeMember, updateMember);
        if (StringUtils.hasText(updateMemberDto.getPassword())) {
            changedDataDto.setPassword("비밀번호가 변경되었습니다.");
        }
        log.info("changedDataDto:{}", changedDataDto);

        redirectAttributes.addFlashAttribute("changedDataDto", changedDataDto); //model에 바로 보냄
        redirectAttributes.addAttribute("memberId", updateMemberDto.getId());
        return "redirect:/myPage/memberModify/complete";
    }

    @Getter @Setter
    @ToString(of = {"name", "email", "password", "telecom", "phoneNumber", "gender"})
    public static class ChangedDataDto {
        private String name;
        private String email;
        private String password;
        private String telecom;
        private String phoneNumber;
        private Gender gender;

        public ChangedDataDto(Member before, Member after) {
            if (!Objects.equals(before.getName(), after.getName())) {
                this.name = after.getName();
            }
            if (!Objects.equals(before.getEmail(), after.getEmail())) {
                this.email = after.getEmail();
            }
            if (!Objects.equals(before.getTelecom(), after.getTelecom())) {
                this.telecom = after.getTelecom();
            }
            if (!Objects.equals(before.getPhoneNumber(), after.getPhoneNumber())) {
                this.phoneNumber = after.getPhoneNumber();
            }
            if (!Objects.equals(before.getGender(), after.getGender())) {
                this.gender = after.getGender();
            }
        }
    }



    /**
     * 개인정보 수정 후, 확인 폼
     */
    @GetMapping("/myPage/memberModify/complete")
    public String memberModifyCompleteForm(@RequestParam("memberId") String memberId, Model model) {
        model.addAttribute("memberId", memberId);
        return "member/mypage/member_modify_complete";
    }



    /**
     * 개인정보 확인/수정 -> 회원 탈퇴하기
     */
    @PostMapping("/delete")
    public String delete(@RequestParam("memberId") String memberId, HttpServletRequest request) {
        int result = memberService.withdraw(memberId);

        if (result == 1) {
            log.info("delete member={}", memberId);
            HttpSession session = request.getSession(false);
            session.invalidate();
            return "redirect:/";
        }

        log.info("delete fail memberId={}", memberId);
        return "redirect:/";
    }



    
}

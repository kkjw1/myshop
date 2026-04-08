package myshop.shop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import myshop.shop.dto.member.LoginCheckMemberDto;
import myshop.shop.dto.member.LoginMemberDto;
import myshop.shop.dto.member.ResetPasswordMemberDto;
import myshop.shop.dto.member.SignUpMemberDto;
import myshop.shop.entity.Member;
import myshop.shop.repository.member.MemberRepository;
import myshop.shop.service.AddressService;
import myshop.shop.service.MemberService;
import myshop.shop.service.RedisService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.util.StringUtils.hasText;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final AddressService addressService;
    private final RedisService redisService;

    //=============================================로그인=============================================
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginMemberDto", new LoginMemberDto());
        return "member/login";
    }

    @GetMapping("login/{loginId}")
    public String loginForm2(@PathVariable("loginId") String id, Model model) {
        model.addAttribute("loginMemberDto", new LoginMemberDto(id));
        return "member/login";
    }

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
     * 로그아웃은 security Spring에서 알아서 처리(세션 쿠키 삭제)
     */


    //=============================================회원가입=============================================
    @GetMapping("/signUp")
    public String signUpForm(Model model) {
        model.addAttribute("signUpMemberDto", new SignUpMemberDto());
        return "member/signUp";
    }

    @GetMapping("/signUp/checkId")
    @ResponseBody
    public boolean checkDuplicateId(@RequestParam("id") String id) {
        return memberService.checkId(id);
    }

    @PostMapping("/signUp")
    public String signUp(@Validated @ModelAttribute("signUpMemberDto") SignUpMemberDto signUpMemberDto, BindingResult bindingResult,
                         RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            log.info("error={}", bindingResult);
            return "member/signUp";
        }

        //핸드폰 인증 체크
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


    @GetMapping("/checkPhoneNumber")
    @ResponseBody
    public Boolean checkDuplicatePhoneNumber(@RequestParam("phoneNumber") String phoneNumber) {
        boolean result = memberService.checkPhoneNumber(phoneNumber);
        log.info("phoneNumber={}, result={}", phoneNumber, result);
        return result;
    }

    @PostMapping("/sendSmsAuth")
    @ResponseBody
    public String sendSmsAuth(@RequestBody SendSmsAuthDto sendSmsAuthDto) {
        log.info("phoneNumber={}", sendSmsAuthDto.getPhoneNumber());
//        String authCode = memberService.smsAuth(sendSmsAuthDto.getPhoneNumber());
        String authCode = "123456";
        redisService.saveData(sendSmsAuthDto.getPhoneNumber(), authCode, 3L);
        return "ok";
    }

    @Getter @Setter
    public static class SendSmsAuthDto {
        private String phoneNumber;
    }

    @Getter @Setter
    public static class CheckAuthCodeDto {
        private String authCode;
        private String phoneNumber;
    }

    @PostMapping("/checkAuthCode")
    @ResponseBody
    public String checkAuthCode(@RequestBody CheckAuthCodeDto checkAuthCodeDto,
                                 HttpServletRequest request) {
        String authCode = checkAuthCodeDto.getAuthCode();
        String phoneNumber = checkAuthCodeDto.getPhoneNumber();
        log.info("authCode:{}, phoneNumber:{}", authCode, phoneNumber);

        String data = redisService.getData(phoneNumber);
        boolean result = authCode.equals(data);
        log.info("result:{}", result);
        if (result) {
            redisService.deleteData(phoneNumber);
            redisService.saveData("confirmedPhone:"+phoneNumber, "SUCCESS", 10L);
            return "ok";
        }
        return "no";
    }

    //=============================================아이디, 비밀번호 찾기=============================================
    @GetMapping("/findMember")
    public String findMemberForm() {
        return "member/find_member";
    }

    @PostMapping("/findMember/checkAuthCode")
    public String findCheckAuthCode(@RequestBody CheckAuthCodeDto checkAuthCodeDto, RedirectAttributes redirectAttributes) {

        String authCode = checkAuthCodeDto.getAuthCode();
        String phoneNumber = checkAuthCodeDto.getPhoneNumber();
        log.info("authCode:{}, phoneNumber:{}", authCode, phoneNumber);

        String data = redisService.getData(phoneNumber);
        boolean result = authCode.equals(data);
        log.info("result:{}", result);
        if (result) {
            redisService.deleteData(phoneNumber);
            Member member = memberRepository.findByPhoneNumber(phoneNumber).orElse(null);
            String memberId = member.getId();
            redisService.saveData("findMemberId:"+ memberId, "SUCCESS", 10L);
            redirectAttributes.addAttribute("memberId", memberId);
            return "redirect:/resetPassword";
        }
        return "member/find_member";
    }

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
}

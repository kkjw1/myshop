package myshop.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myshop.shop.dto.member.LoginMemberDto;
import myshop.shop.dto.member.SignUpMemberDto;
import myshop.shop.entity.Address;
import myshop.shop.entity.Member;
import myshop.shop.repository.address.AddressRepository;
import myshop.shop.repository.member.MemberRepository;
import myshop.shop.service.AddressService;
import myshop.shop.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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

    //===============로그인===============
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

/*    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("loginMemberDto") LoginMemberDto loginMemberDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("login fail");
            return "member/login";
        }


    }*/


    //===============회원가입===============
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
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("error={}", bindingResult);
            return "member/signUp";
        }

        Member signed = memberService.signUp(signUpMemberDto);
        log.info("signUp complete, Member={}", signed);

        redirectAttributes.addAttribute("loginId", signUpMemberDto.getId());
        return "redirect:/login/{loginId}";
    }

    //todo:2.로그인

}

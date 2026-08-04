package myshop.shop.controller.memberWeb;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myshop.shop.dto.inquiry.SaveInquiryDto;
import myshop.shop.dto.member.LoginCheckMemberDto;
import myshop.shop.service.InquiryService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static myshop.shop.controller.memberWeb.MemberController.SessionConst.LOGIN_MEMBER;

@Controller
@RequiredArgsConstructor
@Slf4j
public class InquiryController {

    private final InquiryService inquiryService;

    /**
     * 문의하기
     * 주문 목록/배송 조회 -> 문의하기
     * 취소/반품 내역 -> 문의하기
     */
    @PostMapping("/myPage/inquiry")
    public String inquiry(@ModelAttribute SaveInquiryDto saveInquiryDto) {
        log.info("saveInquiryDto={}", saveInquiryDto);
        inquiryService.saveInquiry(saveInquiryDto);
        return "redirect:/myPage/inquiry";
        //todo: 문의확인 페이지 만들었으니까 문의확인페이지에서 나오는거 확인하
    }

    /**
     * 문의내역 확인 폼
     */
    @GetMapping("/myPage/inquiry")
    public String inquiryListForm(@AuthenticationPrincipal LoginCheckMemberDto loginCheckMemberDto,
                                  HttpServletRequest request, Model model) {
//        new LoginCheckMemberDto().loginCheck(request, model);
//        LoginCheckMemberDto loginCheckMemberDto = (LoginCheckMemberDto) request.getSession().getAttribute(LOGIN_MEMBER);

        // todo: 판매자에서 상품문의 답하는 기능 만든 후, 문의확인페이지에서 데이터 나오는거 제작 해야 됨
        inquiryService.getInquiryList(loginCheckMemberDto.getNo());
        return "member/mypage/inquiry_list";
    }
}

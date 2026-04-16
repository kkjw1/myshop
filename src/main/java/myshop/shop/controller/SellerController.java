package myshop.shop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myshop.shop.dto.member.LoginCheckMemberDto;
import myshop.shop.dto.member.LoginMemberDto;
import myshop.shop.dto.seller.LoginCheckSellerDto;
import myshop.shop.dto.seller.LoginSellerDto;
import myshop.shop.entity.Member;
import myshop.shop.entity.Seller;
import myshop.shop.service.SellerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static myshop.shop.controller.MemberController.SessionConst.LOGIN_MEMBER;
import static myshop.shop.controller.MemberController.SessionConst.LOGIN_SELLER;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;



    /**
     * 판매자 로그인 폼
     */
    @GetMapping("/seller/login")
    public String sellerLoginForm(Model model, @RequestParam(required = false) String redirectURL) {
        model.addAttribute("loginSellerDto", new LoginSellerDto());
        model.addAttribute("redirectURL", redirectURL);
        return "seller/login";
    }



    /**
     * 판매자 로그인
     */
    @PostMapping("/seller/login")
    public String sellerLogin(@Validated @ModelAttribute("loginSellerDto") LoginSellerDto loginSellerDto, BindingResult bindingResult,
                        HttpServletRequest request, @RequestParam(required = false) String redirectURL) {

        if (bindingResult.hasErrors()) {
            log.info("login fail");
            return "seller/login";
        }

        Seller login = sellerService.login(loginSellerDto);

        if (login == null) {
            bindingResult.reject("loginFail","로그인 실패 메시지");
            return "seller/login";
        }

        HttpSession session = request.getSession();
        session.setAttribute(LOGIN_SELLER, new LoginCheckSellerDto(login));

        String target = (redirectURL != null && !redirectURL.isBlank()) ? redirectURL : "/";
        log.info("redirectURL={} target={}", redirectURL, target);
        return "redirect:" + target;
    }


    /**
     * 판매자 대시보드
     */
    @GetMapping("/seller")
    public String sellerHomeForm() {
        return "seller/home";
    }


    /**
     * 상품 관리 폼
     */
    @GetMapping("/seller/item_manage")
    public String item_manage() {
        return "seller/item/item_manage";
    }
    // todo: 상품관리폼부터 하면됨

    @GetMapping("/seller/order_delivery")
    public String order_delivery() {
        return "seller/delivery/order_delivery";
    }

    @GetMapping("/seller/inquiry_manage")
    public String inquiry_manage() {
        return "seller/inquiry/inquiry_manage";
    }

    @GetMapping("/seller/item_new")
    public String item_new() {
        return "seller/item/item_new";
    }


    @GetMapping("/seller/inquiry_reply")
    public String inquiry_reply() {
        return "seller/inquiry/inquiry_reply";
    }

    @GetMapping("/seller/order_delivery_detail")
    public String order_delivery_detail() {
        return "seller/delivery/order_delivery_detail";
    }

}

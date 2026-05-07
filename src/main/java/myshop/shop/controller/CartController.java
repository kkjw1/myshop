package myshop.shop.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import myshop.shop.controller.memberWeb.MemberController;
import myshop.shop.dto.member.LoginCheckMemberDto;
import myshop.shop.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static myshop.shop.controller.memberWeb.MemberController.SessionConst.LOGIN_MEMBER;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;


    /**
     * 상품 상세 폼 -> 장바구니 담기
     */
    @PostMapping("/cart/save")
    @ResponseBody
    public boolean cartSave(@RequestBody SaveCartDto saveCartDto, HttpServletRequest request) {
        LoginCheckMemberDto loginCheckMemberDto = (LoginCheckMemberDto) request.getSession().getAttribute(LOGIN_MEMBER);
        if (loginCheckMemberDto == null) {
            return false;
        }
        saveCartDto.setMemberNo(loginCheckMemberDto.getNo());
        cartService.saveCart(saveCartDto);
        return true;
    }

    @Getter @Setter
    public static class SaveCartDto {
        private Long itemNo;
        private Long memberNo;
        private int count;
    }

}

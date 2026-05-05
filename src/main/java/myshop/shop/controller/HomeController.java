package myshop.shop.controller;

import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myshop.shop.dto.item.MainItemDto;
import myshop.shop.dto.member.LoginCheckMemberDto;
import myshop.shop.dto.member.LoginMemberDto;
import myshop.shop.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final ItemService itemService;


    @GetMapping({"/", "/home"})
    public String home(HttpServletRequest request, Model model) {
        new LoginCheckMemberDto().loginCheck(request, model);
        // 상품들 가져오기
        List<MainItemDto> mainItemDtoList = itemService.getMainItem(4L);

        model.addAttribute("mainItemDtoList", mainItemDtoList);
        return "shop/home";
    }


    /**
     * ExceptionController 테스트
     */
    @GetMapping("/testException")
    @ResponseBody
    public void test() {
        throw new EntityExistsException();
    }
}

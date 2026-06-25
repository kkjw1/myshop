package myshop.shop.controller;

import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import myshop.shop.controller.memberWeb.MemberController;
import myshop.shop.dto.item.DetailItemDto;
import myshop.shop.dto.item.MainItemDto;
import myshop.shop.dto.member.LoginCheckMemberDto;
import myshop.shop.entity.item.Item;
import myshop.shop.entity.item.ItemOption;
import myshop.shop.repository.Item.ItemOptionRepository;
import myshop.shop.repository.Item.ItemRepository;
import myshop.shop.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static myshop.shop.controller.memberWeb.MemberController.SessionConst.LOGIN_MEMBER;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final ItemOptionRepository itemOptionRepository;

    /**
     * 홈 화면
     */
    @GetMapping({"/", "/home"})
    public String homeForm(HttpServletRequest request, Model model) {
        new LoginCheckMemberDto().loginCheck(request, model);
        // 상품들 가져오기
        List<MainItemDto> mainItemDtoList = itemService.getMainItem(4L);

        model.addAttribute("mainItemDtoList", mainItemDtoList);
        return "shop/home";
    }


    /**
     * 상품 상세 폼
     */
    @GetMapping("/item")
    public String itemForm(@RequestParam("itemNo") Long itemNo, HttpServletRequest request, Model model) {
        new LoginCheckMemberDto().loginCheck(request, model);

        DetailItemDto detailItemDto = itemService.getDetailItem(itemNo);
        detailItemDto.setItemNo(itemNo);
        model.addAttribute("detailItemDto", detailItemDto);

        //조회수 증가
        itemService.addViewCount(itemNo);
        return "shop/item_detail";
    }


    /**
     * 상품 상세 폼 -> 바로 구매(1. 로그인 체크, 재고 확인, 재고 선점)
     */
    @PostMapping("/item/checkDirectOrder")
    @ResponseBody
    public String checkDirectOrder(@RequestBody CheckDirectOrderDto checkDirectOrderDto, HttpServletRequest request, Model model) {
        // 로그인 체크
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute(LOGIN_MEMBER) == null) {
            return "loginFail";
        }
        checkDirectOrderDto.setMemberNo(((LoginCheckMemberDto) session.getAttribute(LOGIN_MEMBER)).getNo());

        // 재고 확인
        int stock;
        if (checkDirectOrderDto.getItemOptionNo() == null) {
            Item item = itemRepository.findById(checkDirectOrderDto.getItemNo()).orElse(null);
            stock = item.getTotalStock();
        } else {
            ItemOption itemOption = itemOptionRepository.findById(checkDirectOrderDto.getItemOptionNo()).orElse(null);
            stock = itemOption.getOptionStock();
        }
        if (stock < checkDirectOrderDto.count) {
            return "soldOut";
        }

        // 구매 상품 재고 선점
        itemService.reserveStock(checkDirectOrderDto);

        return "ok";
    }

    @Getter @Setter
    @ToString(of = {"itemNo", "memberNo", "itemOptionNo", "itemImageNo", "count"})
    public static class CheckDirectOrderDto {
        private Long itemNo;
        private Long memberNo;
        private Long itemOptionNo;      // null or Data
        private Long itemImageNo;
        private int count;


        public CheckDirectOrderDto() {
        }

        public CheckDirectOrderDto(Long itemNo, Long memberNo, Long itemOptionNo, Long itemImageNo, int count) {
            this.itemNo = itemNo;
            this.memberNo = memberNo;
            this.itemOptionNo = itemOptionNo;
            this.itemImageNo = itemImageNo;
            this.count = count;
        }
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

package myshop.shop.controller.sellerWeb;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myshop.shop.dto.item.AddItemDto;
import myshop.shop.service.SellerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final SellerService sellerService;


    /**
     * 상품 관리 폼
     */
    @GetMapping("/seller/item_manage")
    public String itemManageForm() {
        return "seller/item/item_manage";
    }



    /**
     * 상품 관리 -> 새 상품 등록 폼
     */
    @GetMapping("/seller/item_new")
    public String itemNewForm() {
        return "seller/item/item_new";
    }


    @PostMapping("/seller/item_new")
    public String itemNew(@ModelAttribute("addItemDto") AddItemDto addItemDto) {

        return "seller/item/item_new";
    }




    //todo: item_option 엔티티 만들기, 아이템 컨트롤러 만들기, 아이템 추가 기능 만들기

}

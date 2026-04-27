package myshop.shop.controller.sellerWeb;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myshop.shop.controller.memberWeb.MemberController;
import myshop.shop.dto.item.AddItemDto;
import myshop.shop.dto.item.AddItemOptionDto;
import myshop.shop.dto.seller.LoginCheckSellerDto;
import myshop.shop.service.FileService;
import myshop.shop.service.SellerService;
import myshop.shop.service.item.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

import static myshop.shop.controller.memberWeb.MemberController.SessionConst.LOGIN_SELLER;


@Controller
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final SellerService sellerService;
    private final FileService fileService;
    private final ItemService itemService;

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
    public String itemNewForm(Model model) {
        model.addAttribute("addItemDto", new AddItemDto());
        return "seller/item/item_new";
    }



    /**
     * 새 상품 등록 폼 -> 상품 등록 완료
     */
    @PostMapping("/seller/item_new")
    public String itemNew(@ModelAttribute("addItemDto") AddItemDto addItemDto, HttpServletRequest request) throws IOException {
        LoginCheckSellerDto loginCheckSellerDto = (LoginCheckSellerDto) request.getSession().getAttribute(LOGIN_SELLER);
        addItemDto.setSellerNo(loginCheckSellerDto.getNo());

        // 이미지 저장
        addItemDto.setMainImagePath(fileService.storeFile(addItemDto.getMainImage()));
        addItemDto.setSubImagesPath(fileService.storeFiles(addItemDto.getSubImages()));

        List<AddItemOptionDto> addItemOptionDtoList = addItemDto.getAddItemOptionDtoList();

        for (AddItemOptionDto addItemOptionDto : addItemOptionDtoList) {
            addItemDto.addTotalStock(addItemOptionDto.getOptionStock());
        }

        log.info("addItemDto={}", addItemDto);
        itemService.saveItem(addItemDto);
        return "redirect:/seller/item_manage";
    }




    //todo: item_option 엔티티 만들기, 아이템 컨트롤러 만들기, 아이템 추가 기능 만들기

}

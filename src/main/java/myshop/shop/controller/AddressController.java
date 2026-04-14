package myshop.shop.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myshop.shop.dto.Address.ManageAddressDto;
import myshop.shop.dto.member.LoginCheckMemberDto;
import myshop.shop.service.AddressService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

import static myshop.shop.controller.MemberController.SessionConst.LOGIN_MEMBER;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;



    /**
     * 베송지 관리 폼
     */
    @GetMapping("/myPage/addressManage")
    public String addressManageForm(HttpServletRequest request, Model model) {
        if (!new LoginCheckMemberDto().loginCheck(request, model)) {
            return "redirect:/login?redirectURL=" + request.getRequestURI();
        }
        List<ManageAddressDto> manageAddressDtoList = addressService.getAddressesByMemberNo(((LoginCheckMemberDto) request.getSession().getAttribute(LOGIN_MEMBER)).getNo());

        if(manageAddressDtoList.isEmpty()) {
            model.addAttribute("manageAddressDtoList", Collections.emptyList());
        } else if (manageAddressDtoList.get(0).getMainAddress() != true) {
            model.addAttribute("manageAddressDtoList", manageAddressDtoList);
        } else {
            ManageAddressDto mainAddress = manageAddressDtoList.get(0);
            List<ManageAddressDto> manageAddressDtos = manageAddressDtoList.subList(1, manageAddressDtoList.size());
            model.addAttribute("mainAddress", mainAddress);
            model.addAttribute("manageAddressDtoList", manageAddressDtos);
        }
        return "member/mypage/address_manage";
    }



    /**
     * 배송지 관리 -> 기본배송지로 설정
     */
    @PostMapping("/myPage/addressManage/updateMain")
    public String addressMainUpdate(@RequestParam("addressNo") Long addressNo, HttpServletRequest request) {
        LoginCheckMemberDto loginCheckMemberDto = (LoginCheckMemberDto) request.getSession().getAttribute(LOGIN_MEMBER);

        addressService.mainUpdate(addressNo, loginCheckMemberDto.getNo());

        return "redirect:/myPage/addressManage";
    }



    /**
     * 배송지 관리 -> 삭제
     */
    @PostMapping("/myPage/addressManage/delete")
    public String addressDelete(@RequestParam("addressNo") Long addressNo) {
        addressService.deleteAddress(addressNo);

        return "redirect:/myPage/addressManage";
    }


    /**
     * 배송지 관리 -> 수정 폼
     * /myPage/addressManage/update?addressNo=
     */
    @GetMapping("/myPage/addressManage/update")
    public String addressUpdateForm(@RequestParam("addressNo") Long addressNo, Model model, HttpServletRequest request) {

        if (!new LoginCheckMemberDto().loginCheck(request, model)) {
            return "redirect:/login?redirectURL=" + request.getRequestURI();
        }

/*        Address address = addressRepository.findByNo(addressNo);
        UpdateAddress updateAddress = new UpdateAddress(
                address.getAddressName(),
                address.getRecipientName(),
                address.getPhoneNumber(),
                address.getPostcode(),
                address.getRoadAddress(),
                address.getDetailAddress(),
                address.getDeliveryRequest());

        updateAddress.setAddressNo(addressNo);
        model.addAttribute("updateAddress", updateAddress);*/
        return "member/mypage/address_manage_update";
    }










    /**
     * 배송지 관리 -> 배송지 추가 폼
     */

}



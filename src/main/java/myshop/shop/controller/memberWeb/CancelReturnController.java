package myshop.shop.controller.memberWeb;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myshop.shop.dto.cancelRequest.SaveCancelRequestDto;
import myshop.shop.dto.returnRequest.SaveReturnRequestDto;
import myshop.shop.service.CancelRequestService;
import myshop.shop.service.ReturnRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CancelReturnController {

    private final CancelRequestService cancelRequestService;
    private final ReturnRequestService returnRequestService;


    /**
     * 주문 취소 신청
     * 주문 목록/배송 조회 -> 주문 취소 신청
     */
    @PostMapping("/myPage/cancel_request")
    public String cancelRequestForm(@ModelAttribute SaveCancelRequestDto saveCancelRequestDto, RedirectAttributes redirectAttributes) {
        log.info("saveCancelRequestDto={}", saveCancelRequestDto);

        cancelRequestService.saveCancelRequest(saveCancelRequestDto);
        // todo: redirect로 취소 반품 페이지로 가기
        return "redirect:/myPage/orderList";
    }



    /**
     * 반품 신청
     * 주문 목록/배송 조회 -> 반품 신청
     */
    @PostMapping("/myPage/return_request")
    public String returnRequestForm(@ModelAttribute SaveReturnRequestDto saveReturnRequestDto, RedirectAttributes redirectAttributes) {
        log.info("saveReturnRequestDto={}", saveReturnRequestDto);

        returnRequestService.saveReturnRequest(saveReturnRequestDto);
        // todo: redirect로 취소 반품 페이지로 가기
        return "redirect:/myPage/orderList";
    }

}

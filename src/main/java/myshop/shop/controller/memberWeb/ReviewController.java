package myshop.shop.controller.memberWeb;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myshop.shop.dto.review.SaveReviewDto;
import myshop.shop.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;


    /**
     * 리뷰 등록
     * 주문 목록/배송 조회 -> 리뷰 작성하기
     */
    @PostMapping("/myPage/review")
    public String saveReview(@ModelAttribute SaveReviewDto saveReviewDto) {
       log.info("saveReviewDto={}", saveReviewDto);

       reviewService.saveReview(saveReviewDto);

        // todo: 상품 상세에서 리뷰와 문의 내역 뜨는 기능 추가하기
       return "redirect:/myPage/orderList";
    }
}

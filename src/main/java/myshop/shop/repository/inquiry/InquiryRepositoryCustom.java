package myshop.shop.repository.inquiry;

import myshop.shop.controller.HomeItemController;
import myshop.shop.controller.HomeItemController.DetailItemInquiryDto;
import myshop.shop.dto.inquiry.CheckInquiryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InquiryRepositoryCustom {
    /**
     * 문의내역 확인
     */
    List<CheckInquiryDto> getCheckInquiryDtoList(Long memberNo);


    /**
     * 상품 문의 데이터
     * 상품상세 -> 상품 문의
     */
    Page<DetailItemInquiryDto> findDetailItemInquiry(Pageable pageable, Long itemNo);
}

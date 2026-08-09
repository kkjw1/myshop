package myshop.shop.repository.inquiry;

import myshop.shop.dto.inquiry.CheckInquiryDto;

import java.util.List;

public interface InquiryRepositoryCustom {
    /**
     * 문의내역 확인
     */
    List<CheckInquiryDto> getCheckInquiryDtoList(Long memberNo);
}

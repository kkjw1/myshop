package myshop.shop.service;

import lombok.RequiredArgsConstructor;
import myshop.shop.dto.inquiry.CheckInquiryDto;
import myshop.shop.dto.inquiry.SaveInquiryDto;
import myshop.shop.entity.inquiry.Inquiry;
import myshop.shop.entity.inquiry.InquiryCategory;
import myshop.shop.entity.inquiry.InquiryStatus;
import myshop.shop.entity.item.Item;
import myshop.shop.repository.Item.ItemRepository;
import myshop.shop.repository.inquiry.InquiryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final ItemRepository itemRepository;

    /**
     * 문의하기
     * 주문 목록/배송 조회 -> 문의하기
     * 취소/반품 내역 -> 문의하기
     */
    public void saveInquiry(SaveInquiryDto saveInquiryDto) {
        Item itemProxy = itemRepository.getReferenceById(saveInquiryDto.getItemNo());

        inquiryRepository.save(new Inquiry(itemProxy,
                saveInquiryDto.getMemberNo(),
                saveInquiryDto.getOptionName(),
                saveInquiryDto.getInquiryCategory(),
                saveInquiryDto.getTitle(),
                saveInquiryDto.getContent(),
                InquiryStatus.답변대기));
    }

    /**
     * 상품/기타 문의하기
     * 아이템 상세 -> 문의하기
     */
    public void saveProductInquiry(SaveInquiryDto saveInquiryDto) {
        Item itemProxy = itemRepository.getReferenceById(saveInquiryDto.getItemNo());

        inquiryRepository.save(new Inquiry(itemProxy,
                saveInquiryDto.getMemberNo(),
                saveInquiryDto.getOptionName(),
                InquiryCategory.PRODUCT,
                saveInquiryDto.getTitle(),
                saveInquiryDto.getContent(),
                InquiryStatus.답변대기));
    }



    /**
     * 문의내역 확인 폼
     */
    public List<CheckInquiryDto> getInquiryList(Long memberNo) {
        return inquiryRepository.getCheckInquiryDtoList(memberNo);
    }
}

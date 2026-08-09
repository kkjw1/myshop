package myshop.shop.repository.inquiry;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import myshop.shop.dto.inquiry.CheckInquiryDto;


import java.util.List;

import static myshop.shop.entity.inquiry.QInquiry.inquiry;
import static myshop.shop.entity.item.QItem.item;

@RequiredArgsConstructor
public class InquiryRepositoryImpl implements InquiryRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CheckInquiryDto> getCheckInquiryDtoList(Long memberNo) {

        return queryFactory
                .select(Projections.fields(CheckInquiryDto.class,
                        inquiry.no,
                        inquiry.inquiryCategory,
                        item.name.as("itemName"),
                        inquiry.optionName,
                        inquiry.content,
                        inquiry.inquiryStatus,
                        inquiry.memberNo,
                        inquiry.answerContent
                ))
                .from(inquiry)
                .leftJoin(inquiry.item, item)
                .where(inquiry.memberNo.eq(memberNo))
                .orderBy(inquiry.createdDate.desc())
                .fetch();
    }

}

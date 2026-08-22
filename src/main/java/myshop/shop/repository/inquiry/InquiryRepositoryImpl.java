package myshop.shop.repository.inquiry;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import myshop.shop.controller.HomeItemController;
import myshop.shop.controller.HomeItemController.DetailItemInquiryDto;
import myshop.shop.dto.inquiry.CheckInquiryDto;
import myshop.shop.entity.inquiry.InquiryCategory;
import myshop.shop.entity.inquiry.InquiryStatus;
import myshop.shop.entity.inquiry.QInquiry;
import myshop.shop.entity.member.QMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;


import java.util.List;

import static myshop.shop.entity.inquiry.QInquiry.inquiry;
import static myshop.shop.entity.item.QItem.item;
import static myshop.shop.entity.member.QMember.member;

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
                        inquiry.title,
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

    @Override
    public Page<DetailItemInquiryDto> findDetailItemInquiry(Pageable pageable, Long itemNo) {
        List<DetailItemInquiryDto> content = queryFactory
                .select(Projections.fields(DetailItemInquiryDto.class,
                        inquiry.memberNo,
                        item.name.as("itemName"),
                        inquiry.optionName,
                        inquiry.inquiryCategory,
                        inquiry.title,
                        inquiry.content,
                        inquiry.inquiryStatus,
                        inquiry.answerContent
                ))
                .from(inquiry)
                .leftJoin(inquiry.item, item)
                .where(inquiry.item.no.eq(itemNo))
                .orderBy(inquiry.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = queryFactory
                .select(inquiry.count())
                .from(inquiry)
                .where(inquiry.item.no.eq(itemNo));

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

}

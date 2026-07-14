package myshop.shop.repository.cancelRequest;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import myshop.shop.dto.cancelRequest.ManageCancelReturnDto;
import myshop.shop.entity.QOrderItem;
import myshop.shop.entity.cancelRequest.CancelReasonCode;
import myshop.shop.entity.cancelRequest.CancelRequestStatus;
import myshop.shop.entity.cancelRequest.QCancelRequest;
import myshop.shop.entity.item.QItem;
import myshop.shop.entity.returnRequest.QReturnRequest;
import myshop.shop.entity.returnRequest.ReturnReasonCode;
import myshop.shop.entity.returnRequest.ReturnRequestStatus;

import java.math.BigDecimal;
import java.net.Authenticator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static myshop.shop.entity.QOrderItem.orderItem;
import static myshop.shop.entity.cancelRequest.QCancelRequest.cancelRequest;
import static myshop.shop.entity.item.QItem.item;
import static myshop.shop.entity.returnRequest.QReturnRequest.returnRequest;

@RequiredArgsConstructor
public class CancelRequestRepositoryImpl implements  CancelRequestRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ManageCancelReturnDto> findCancelReturnList(Long memberNo) {
        List<ManageCancelReturnDto> manageCancelReturnDtoList = new ArrayList<>();

        List<ManageCancelReturnDto> cancelDto = queryFactory
                .select(Projections.fields(ManageCancelReturnDto.class,
                        orderItem.no.as("orderItemNo"),
                        item.no.as("itemNo"),
                        cancelRequest.price.as("price"),
                        cancelRequest.cancelRequestStatus.as("cancelRequestStatus"),
                        cancelRequest.cancelReasonCode.as("cancelReasonCode"),
                        cancelRequest.reasonDetail.as("reasonDetail"),
                        orderItem.itemName.as("itemName"),
                        orderItem.optionName.as("optionName"),
                        cancelRequest.createdDate.as("requestTime"),
                        cancelRequest.count
                ))
                .from(cancelRequest)
                .join(cancelRequest.orderItem, orderItem)
                .join(orderItem.item, item)
                .where(cancelRequest.member.no.eq(memberNo))
                .fetch();

        manageCancelReturnDtoList.addAll(cancelDto);

        List<ManageCancelReturnDto> returnDto = queryFactory
                .select(Projections.fields(ManageCancelReturnDto.class,
                        orderItem.no.as("orderItemNo"),
                        item.no.as("itemNo"),
                        returnRequest.price.as("price"),
                        returnRequest.returnRequestStatus.as("returnRequestStatus"),
                        returnRequest.returnReasonCode.as("returnReasonCode"),
                        returnRequest.reasonDetail.as("reasonDetail"),
                        orderItem.itemName.as("itemName"),
                        orderItem.optionName.as("optionName"),
                        returnRequest.createdDate.as("requestTime"),
                        returnRequest.count
                ))
                .from(returnRequest)
                .join(returnRequest.orderItem, orderItem)
                .join(orderItem.item, item)
                .where(returnRequest.member.no.eq(memberNo))
                .fetch();

        manageCancelReturnDtoList.addAll(returnDto);

        // 내림차순
        manageCancelReturnDtoList.sort(Comparator.comparing(ManageCancelReturnDto::getRequestTime).reversed());
        return manageCancelReturnDtoList;
    }

}

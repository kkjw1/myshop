package myshop.shop.repository.orderItem;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myshop.shop.dto.order.DetailOrderDto;
import myshop.shop.dto.order.DetailOrderItemDto;
import myshop.shop.entity.QOrderItem;
import myshop.shop.entity.delivery.QDelivery;
import myshop.shop.entity.item.QItem;
import myshop.shop.entity.order.QOrder;

import java.util.List;

import static myshop.shop.entity.QOrderItem.orderItem;
import static myshop.shop.entity.delivery.QDelivery.delivery;
import static myshop.shop.entity.order.QOrder.order;

@RequiredArgsConstructor
@Slf4j
public class OrderItemRepositoryImpl implements OrderItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public DetailOrderDto getDetailOrder(Long orderNo) {
        List<DetailOrderItemDto> DetailOrderItemDtoList = queryFactory
                .select(Projections.constructor(DetailOrderItemDto.class,
                        orderItem.item.no.as("itemNo"),
                        orderItem.count,
                        orderItem.price,
                        orderItem.imageUrl,
                        orderItem.itemName,
                        orderItem.optionName)
                )
                .from(orderItem)
                .where(orderItem.order.no.eq(orderNo))
                .fetch();

        DetailOrderDto detailOrderDto = queryFactory
                .select(Projections.constructor(DetailOrderDto.class,
                                delivery.recipientName,
                                delivery.recipientPhone.as("phoneNumber"),
                                delivery.postcode,
                                delivery.roadAddress,
                                delivery.detailAddress,
                                delivery.deliveryRequest,
                                delivery.deliveryFee,
                                order.totalPrice.as("totalOrderPrice"),
                                order.createdDate.as("orderTime")
                        ))
                .from(orderItem)
                .leftJoin(orderItem.delivery, delivery)
                .leftJoin(orderItem.order, order)
                .where(orderItem.order.no.eq(orderNo))
                .groupBy(orderItem.order.no)
                .fetchOne();

        detailOrderDto.setOrderNo(orderNo);
        for (DetailOrderItemDto detailOrderItemDto : DetailOrderItemDtoList) {
            detailOrderDto.addTotalProductPrice(detailOrderItemDto.getPrice());
        }
        detailOrderDto.setDetailOrderItemDtoList(DetailOrderItemDtoList);
        log.info("detailOrderDto={}", detailOrderDto);
        return detailOrderDto;
    }
}

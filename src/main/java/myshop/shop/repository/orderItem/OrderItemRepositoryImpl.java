package myshop.shop.repository.orderItem;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myshop.shop.dto.order.DetailOrderDto;
import myshop.shop.dto.order.DetailOrderItemDto;
import myshop.shop.dto.order.ManageOrderDto;
import myshop.shop.dto.order.ManageOrderItemDto;
import myshop.shop.entity.QOrderItem;
import myshop.shop.entity.delivery.QDelivery;
import myshop.shop.entity.order.QOrder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static myshop.shop.entity.QOrderItem.orderItem;
import static myshop.shop.entity.delivery.QDelivery.delivery;
import static myshop.shop.entity.item.QItem.item;
import static myshop.shop.entity.order.QOrder.order;

@RequiredArgsConstructor
@Slf4j
public class OrderItemRepositoryImpl implements OrderItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public DetailOrderDto getDetailOrder(Long orderNo) {

        DetailOrderDto detailOrderDto = queryFactory
                .select(Projections.fields(DetailOrderDto.class,
                        delivery.recipientName,
                        delivery.recipientPhone.as("phoneNumber"),
                        delivery.postcode,
                        delivery.roadAddress,
                        delivery.detailAddress,
                        delivery.deliveryRequest,
                        delivery.deliveryFee,
                        order.createdDate.as("orderTime")
                ))
                .from(orderItem)
                .leftJoin(orderItem.delivery, delivery)
                .leftJoin(orderItem.order, order)
                .where(order.no.eq(orderNo))
                .fetchFirst();

        List<DetailOrderItemDto> detailOrderItemDtoList = queryFactory
                .select(Projections.fields(DetailOrderItemDto.class,
                        orderItem.count,
                        orderItem.price.as("totalPrice"),
                        orderItem.imageUrl,
                        orderItem.itemName,
                        orderItem.optionName
                ))
                .from(orderItem)
                .leftJoin(orderItem.order, order)
                .where(order.no.eq(orderNo))
                .fetch();

        for (DetailOrderItemDto detailOrderItemDto : detailOrderItemDtoList) {
            detailOrderDto.getDetailOrderItemDtoList().add(detailOrderItemDto);
        }
        return detailOrderDto;
    }

    @Override
    public List<ManageOrderDto> getManageOrder(Long memberNo) {
        List<ManageOrderDto> manageOrderDtoList = queryFactory
                .select(Projections.fields(ManageOrderDto.class,
                        order.no.as("orderNo"),
                        order.createdDate.as("orderTime")
                ))
                .from(order)
                .where(order.member.no.eq(memberNo))
                .fetch();

        for (ManageOrderDto manageOrderDto : manageOrderDtoList) {
            Long orderNo = manageOrderDto.getOrderNo();

            List<ManageOrderItemDto> manageOrderItemDtoList = queryFactory
                    .select(Projections.fields(ManageOrderItemDto.class,
                            delivery.deliveryStatus,
                            orderItem.imageUrl,
                            orderItem.itemName,
                            orderItem.optionName,
                            orderItem.price.as("totalPrice"),
                            orderItem.count
                    ))
                    .from(orderItem)
                    .leftJoin(orderItem.delivery, delivery)
                    .where(orderItem.order.no.eq(orderNo))
                    .fetch();

            manageOrderDto.setManageOrderItemDtoList(manageOrderItemDtoList);
        }

        return manageOrderDtoList;
    }
}

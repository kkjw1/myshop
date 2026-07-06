package myshop.shop.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import myshop.shop.dto.delivery.OrderDeliveryDto;
import myshop.shop.dto.delivery.OrderItemDeliveryDto;
import myshop.shop.dto.delivery.UpdateDeliveryDto;
import myshop.shop.entity.delivery.Delivery;
import myshop.shop.repository.delivery.DeliveryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static myshop.shop.entity.QOrderItem.orderItem;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    /**
     * 주문 물품 가져오기
     * 판매자 센터 -> 주문/배송 관리 폼
     */
    public List<OrderDeliveryDto> findAllDelivery(Long sellerNo) {
        // delivery 값 가져오기
        Set<Long> orderNoSet = deliveryRepository.getOrderNoSet(sellerNo);
        return deliveryRepository.getOrderDeliveryList(orderNoSet);
    }


    /**
     * 주문 상품들 가져오기
     * 주문/배송 관리 폼 -> 관리
     */
    public List<OrderItemDeliveryDto> manageOrderDelivery(Long orderNo) {
        return deliveryRepository.getOrderItemDeliveryDtoList(orderNo);
    }


    /**
     * 상품 배송사항 변경
     * 관리 -> 변경사항 저장
     */
    public void updateOrderItemDelivery(List<UpdateDeliveryDto> updateDeliveryDtoList) {
        for (UpdateDeliveryDto updateOrderDeliveryDto : updateDeliveryDtoList) {
            Delivery delivery = queryFactory
                    .select(orderItem.delivery)
                    .from(orderItem)
                    .where(orderItem.no.eq(updateOrderDeliveryDto.getOrderItemNo()))
                    .fetchFirst();

            delivery.updateCourier(updateOrderDeliveryDto.getCourier());
            delivery.updateTrackingNumber(updateOrderDeliveryDto.getTrackingNumber());
            delivery.updateDeliveryStatus(updateOrderDeliveryDto.getDeliveryStatus());

            em.flush();
            em.clear();
        }

    }
}

package myshop.shop.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import myshop.shop.dto.order.AddOrderItemDto;
import myshop.shop.dto.order.AddOrderDto;
import myshop.shop.dto.order.DetailOrderDto;
import myshop.shop.entity.OrderItem;
import myshop.shop.entity.delivery.Delivery;
import myshop.shop.entity.delivery.DeliveryStatus;
import myshop.shop.entity.item.Item;
import myshop.shop.entity.member.Member;
import myshop.shop.entity.order.Order;
import myshop.shop.entity.order.OrderStatus;
import myshop.shop.repository.Item.ItemRepository;
import myshop.shop.repository.delivery.DeliveryRepository;
import myshop.shop.repository.member.MemberRepository;
import myshop.shop.repository.order.OrderRepository;
import myshop.shop.repository.orderItem.OrderItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final EntityManager em;
    private final OrderItemRepository orderItemRepository;
    private final DeliveryRepository deliveryRepository;

    /**
     * 주문 저장
     */
    public Order saveOrder(Long memberNo, AddOrderDto addOrderDto) {
        Member memberProxy = memberRepository.getReferenceById(memberNo);

        // Order 저장
        Order savedOrder = orderRepository.save(new Order(memberProxy,
                OrderStatus.결제완료,
                addOrderDto.getTotalOrderPrice()));


        // Delivery, OrderItem 저장
        List<AddOrderItemDto> addOrderItemDtoList = addOrderDto.getAddOrderItemDtoList();
        for (AddOrderItemDto addOrderItemDto : addOrderItemDtoList) {
            Delivery savedDelivery = deliveryRepository.save(new Delivery(null, null, DeliveryStatus.배송준비중,
                    addOrderDto.getRecipientName(), addOrderDto.getPhoneNumber(),
                    addOrderDto.getPostcode(), addOrderDto.getRoadAddress(), addOrderDto.getDetailAddress(),
                    addOrderDto.getDeliveryFee(), addOrderDto.getDeliveryRequest()));
            Item itemProxy = itemRepository.getReferenceById(addOrderItemDto.getItemNo());
            OrderItem orderItem = new OrderItem(savedOrder,
                    itemProxy,
                    savedDelivery,
                    addOrderItemDto.getCount(),
                    addOrderItemDto.getPrice(),
                    addOrderItemDto.getImageUrl(),
                    addOrderItemDto.getItemName(),
                    addOrderItemDto.getOptionName());
            orderItem.updateOrder(savedOrder);
            orderItemRepository.save(orderItem);
        }

        return savedOrder;
    }


    /**
     * 주문 상세 내역 불러오기
     */
    public DetailOrderDto getOrder(Long orderNo) {
        return orderItemRepository.getDetailOrder(orderNo);
    }

}

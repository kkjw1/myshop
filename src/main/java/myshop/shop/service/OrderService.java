package myshop.shop.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import myshop.shop.dto.AddOrderItemDto;
import myshop.shop.dto.order.AddOrderDto;
import myshop.shop.entity.OrderItem;
import myshop.shop.entity.item.Item;
import myshop.shop.entity.member.Member;
import myshop.shop.entity.order.Order;
import myshop.shop.entity.order.OrderStatus;
import myshop.shop.repository.Item.ItemRepository;
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

    /**
     * 주문 저장
     */
    public Order saveOrder(Long memberNo, AddOrderDto addOrderDto) {
        Member memberProxy = memberRepository.getReferenceById(memberNo);
        Order savedOrder = orderRepository.save(new Order(memberProxy,
                OrderStatus.결제완료,
                addOrderDto.getTotalOrderPrice(),
                addOrderDto.getPostcode(),
                addOrderDto.getRoadAddress(),
                addOrderDto.getDetailAddress()));


        List<AddOrderItemDto> addOrderItemDtoList = addOrderDto.getAddOrderItemDtoList();
        for (AddOrderItemDto addOrderItemDto : addOrderItemDtoList) {
            Item itemProxy = itemRepository.getReferenceById(addOrderItemDto.getItemNo());
            OrderItem orderItem = new OrderItem(savedOrder,
                    itemProxy,
                    addOrderItemDto.getCount(),
                    addOrderItemDto.getPrice(),
                    addOrderItemDto.getTotalPrice(),
                    addOrderItemDto.getImageUrl(),
                    addOrderItemDto.getItemName(),
                    addOrderItemDto.getOptionName());
            orderItem.updateOrder(savedOrder);
            orderItemRepository.save(orderItem);
        }

        return savedOrder;
    }
}

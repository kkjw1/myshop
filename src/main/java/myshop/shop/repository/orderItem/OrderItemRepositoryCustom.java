package myshop.shop.repository.orderItem;

import myshop.shop.dto.order.DetailOrderDto;

public interface OrderItemRepositoryCustom {
    DetailOrderDto getDetailOrder(Long orderNo);
}

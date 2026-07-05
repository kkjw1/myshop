package myshop.shop.repository.delivery;

import myshop.shop.dto.delivery.OrderDeliveryDto;

import java.util.List;
import java.util.Set;

public interface DeliveryRepositoryCustom {
    Set<Long> getOrderNoSet(Long sellerNo);
    List<OrderDeliveryDto> getOrderDeliveryList(Set<Long> orderNoList);
}

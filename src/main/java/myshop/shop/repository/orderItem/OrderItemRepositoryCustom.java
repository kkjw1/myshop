package myshop.shop.repository.orderItem;

import myshop.shop.dto.order.DetailOrderDto;
import myshop.shop.dto.order.ManageOrderDto;

import java.util.List;

public interface OrderItemRepositoryCustom {
    DetailOrderDto getDetailOrder(Long orderNo);
    List<ManageOrderDto> getManageOrder(Long memberNo);
}

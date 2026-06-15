package myshop.shop.repository.order;

import myshop.shop.controller.HomeController;
import myshop.shop.controller.HomeController.CheckDirectOrderDto;
import myshop.shop.dto.order.DirectOrderDto;

public interface OrderRepositoryCustom {
    DirectOrderDto getDirectOrder(CheckDirectOrderDto checkDirectOrderDto);
}

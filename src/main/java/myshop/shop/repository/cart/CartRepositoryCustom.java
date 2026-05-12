package myshop.shop.repository.cart;

import myshop.shop.dto.cart.ManageCartDto;

import java.util.List;

public interface CartRepositoryCustom {
    List<ManageCartDto> getManageCartList(Long memberNo);

    ManageCartDto getManageCart(Long cartNo);
}

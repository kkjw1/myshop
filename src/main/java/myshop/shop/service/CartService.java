package myshop.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import myshop.shop.controller.CartController.SaveCartDto;

import myshop.shop.entity.Cart;
import myshop.shop.entity.item.Item;
import myshop.shop.entity.member.Member;
import myshop.shop.repository.Item.ItemRepository;
import myshop.shop.repository.cart.CartRepository;
import myshop.shop.repository.member.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 장바구니 저장
     */
    public void saveCart(SaveCartDto saveCartDto) {
        Member memberProxy = memberRepository.getReferenceById(saveCartDto.getMemberNo());
        Item itemProxy = itemRepository.getReferenceById(saveCartDto.getItemNo());
        Cart cart = new Cart(memberProxy,
                itemProxy,
                saveCartDto.getCount());

        cartRepository.save(cart);
    }

}

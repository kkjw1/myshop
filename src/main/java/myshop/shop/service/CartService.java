package myshop.shop.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import myshop.shop.dto.cart.SaveCartDto;
import myshop.shop.entity.Cart;
import myshop.shop.entity.item.Item;
import myshop.shop.entity.item.ItemOption;
import myshop.shop.entity.member.Member;
import myshop.shop.repository.Item.ItemOptionRepository;
import myshop.shop.repository.Item.ItemRepository;
import myshop.shop.repository.cart.CartRepository;
import myshop.shop.repository.member.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final EntityManager em;

    /**
     * 장바구니 저장
     */
    public void saveCart(SaveCartDto saveCartDto) {
        log.info("saveCartDto={}", saveCartDto);
        Member memberProxy = memberRepository.getReferenceById(saveCartDto.getMemberNo());
        Item itemProxy = itemRepository.getReferenceById(saveCartDto.getItemNo());
        Long itemOptionNo = saveCartDto.getItemOptionNo();
        int count = saveCartDto.getCount();


        List<Cart> findCart = em.createQuery("select c from Cart c where c.item=:item and c.member=:member", Cart.class)
                .setParameter("item", itemProxy)
                .setParameter("member", memberProxy)
                .getResultList();
        if (findCart.isEmpty()) {
            if (itemOptionNo != null) {
                ItemOption itemOptionProxy = itemOptionRepository.getReferenceById(itemOptionNo);
                Cart cart = new Cart(memberProxy,
                        itemProxy,
                        count,
                        itemOptionProxy);
                cartRepository.save(cart);
            }
            else {
                Cart cart = new Cart(memberProxy,
                        itemProxy,
                        count);
                cartRepository.save(cart);
            }
        } else {
            findCart.get(0).updateCount(count);
        }
    }

}

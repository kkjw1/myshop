package myshop.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myshop.shop.dto.member.LoginCheckMemberDto;
import myshop.shop.entity.item.Item;
import myshop.shop.entity.member.Member;
import myshop.shop.repository.Item.ItemRepository;
import myshop.shop.repository.Item.ItemRepositoryImpl;
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
    public void saveCart(Long memberNo, Long itemNo) {
        Member memberProxy = memberRepository.getReferenceById(memberNo);
        Item itemProxy = itemRepository.getReferenceById(itemNo);

    }

}

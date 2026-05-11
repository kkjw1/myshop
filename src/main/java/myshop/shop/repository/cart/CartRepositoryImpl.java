package myshop.shop.repository.cart;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import myshop.shop.dto.cart.ManageCartDto;

import java.util.List;

import static myshop.shop.entity.QCart.cart;
import static myshop.shop.entity.item.QItem.item;
import static myshop.shop.entity.item.QItemImage.itemImage;
import static myshop.shop.entity.item.QItemOption.itemOption;

@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public List<ManageCartDto> getManageCartList(Long memberNo) {

        return queryFactory
                .select(Projections.fields(ManageCartDto.class,
                        cart.no.as("cartNo"),
                        item.no.as("itemNo"),
                        itemOption.no.as("itemOptionNo"),
                        item.name,
                        itemImage.imageUrl.as("imagePath"),
                        cart.count,
                        item.totalStock,
                        itemOption.optionStock,
                        item.price.as("originPrice"),
                        itemOption.additionalPrice.as("optionPrice"),
                        itemOption.name.as("optionName")))
                .from(cart)
                .leftJoin(cart.item, item)
                .leftJoin(cart.itemOption, itemOption)
                .leftJoin(cart.itemImage, itemImage)
                .where(cart.member.no.eq(memberNo))
                .fetch();
    }

}

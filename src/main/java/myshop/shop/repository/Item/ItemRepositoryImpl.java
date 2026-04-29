package myshop.shop.repository.Item;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import myshop.shop.dto.item.ManageItemDto;
import myshop.shop.dto.item.SearchItemDto;

import myshop.shop.entity.item.ItemStatus;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;



import java.util.List;

import static myshop.shop.entity.QSeller.seller;
import static myshop.shop.entity.item.QItem.item;
import static myshop.shop.entity.item.QItemImage.itemImage;


@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ManageItemDto> searchItemPage(Pageable pageable, SearchItemDto searchItemDto) {

        List<ManageItemDto> content = queryFactory
                .select(Projections.fields(ManageItemDto.class,
                        item.no.as("itemNo"),
                        itemImage.imageUrl.as("mainImagePath"),
                        item.name,
                        item.price,
                        item.totalStock,
                        item.itemStatus,
                        item.createdDate))
                .from(itemImage)
                .join(itemImage.item, item)
                .join(item.seller, seller)
                .where(sellerNoEq(searchItemDto.getSellerNo()), itemNoEq(searchItemDto.getItemNo()),
                        itemNameLike(searchItemDto.getName()), itemStatusEq(searchItemDto.getItemStatus()),
                        itemImage.isMain.eq(true))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = queryFactory
                .select(item.count())
                .from(item)
                .leftJoin(item.seller, seller)
                .leftJoin(item.itemImages, itemImage)
                .where(sellerNoEq(searchItemDto.getSellerNo()), itemNoEq(searchItemDto.getItemNo()),
                        itemNameLike(searchItemDto.getName()), itemStatusEq(searchItemDto.getItemStatus()),
                        itemImage.isMain.eq(true));

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    private BooleanExpression sellerNoEq(Long sellerNo) {
        return sellerNo != null ? seller.no.eq(sellerNo) : null;
    }

    private BooleanExpression itemNoEq(Long itemNo) {
        return itemNo != null ? item.no.eq(itemNo) : null;
    }

    private BooleanExpression itemNameLike(String name) {
        return name != null ? item.name.contains(name) : null;
    }

    private BooleanExpression itemStatusEq(ItemStatus itemStatus) {
        return itemStatus != null ? item.itemStatus.eq(itemStatus) : null;
    }
}

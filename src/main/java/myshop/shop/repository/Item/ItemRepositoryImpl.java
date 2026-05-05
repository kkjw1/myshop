package myshop.shop.repository.Item;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import lombok.RequiredArgsConstructor;
import myshop.shop.controller.sellerWeb.ItemController;
import myshop.shop.controller.sellerWeb.ItemController.BulkModifyItemDto;
import myshop.shop.dto.item.*;

import myshop.shop.entity.item.Item;
import myshop.shop.entity.item.ItemStatus;


import myshop.shop.entity.item.QItem;
import myshop.shop.entity.item.QItemOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;


import java.util.ArrayList;
import java.util.List;

import static myshop.shop.entity.QSeller.seller;
import static myshop.shop.entity.item.QItem.item;
import static myshop.shop.entity.item.QItemImage.itemImage;
import static myshop.shop.entity.item.QItemOption.itemOption;


@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    /**
     * 상품 관리 데이터 불러오기
     * Item & ItemImage -> ManageItemDto
     */
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


    /**
     * 상품 일괄 수정
     */
    @Override
    public long bulkItemStatusDiscount(BulkModifyItemDto bulkModifyItemDto) {
        JPAUpdateClause query = queryFactory.update(item);

        if(bulkModifyItemDto.getItemStatus() != null) {
            query.set(item.itemStatus, bulkModifyItemDto.getItemStatus());
        }
        if(bulkModifyItemDto.getDiscount() != null) {
            query.set(item.discount, bulkModifyItemDto.getDiscount());
        }

        return query.where(item.no.in(bulkModifyItemDto.getItemNos()))
                .execute();
    }


    /**
     * 메인 화면 상품 출력
     * viewCount desc
     */
    @Override
    public List<MainItemDto> findMainItem(Long limit) {

        return queryFactory
                .select(Projections.fields(MainItemDto.class,
                        itemImage.imageUrl.as("mainImagePath"),
                        item.name,
                        item.price,
                        item.discount,
                        item.viewCount))
                .from(itemImage)
                .join(itemImage.item, item)
                .where(itemImage.isMain.eq(true), item.itemStatus.eq(ItemStatus.판매중))
                .orderBy(item.viewCount.desc())
                .limit(limit)
                .fetch();
    }



    /**
     * 상품 클릭 -> 상품 상세 출력
     * Item & ItemOption -> DetailItemDto
     */
    @Override
    public DetailItemDto findDetailItem(Long itemNo) {
        Item item = queryFactory
                .select(QItem.item)
                .from(QItem.item)
                .leftJoin(QItem.item.itemOptions, itemOption).fetchJoin()
                .where(QItem.item.no.eq(itemNo))
                .fetchOne();

        if (item != null) {
            return new DetailItemDto(
                    item.getItemCategory(),
                    item.getName(),
                    item.getPrice(),
                    item.getDiscount(),
                    item.getTotalStock(),
                    item.getItemOptions(),
                    item.getContent()
            );
        }
        return null;
    }



    /**
     * 이미지 주소들 가져오기
     * sortOrder ASC
     */
    @Override
    public List<String> getImageUrls(Long itemNo) {
        return queryFactory
                .select(itemImage.imageUrl)
                .from(itemImage)
                .where(itemImage.item.no.eq(itemNo))
                .orderBy(itemImage.sortOrder.asc())
                .fetch();
    }
}

package myshop.shop.repository.Item;

import myshop.shop.controller.sellerWeb.ItemController;
import myshop.shop.dto.item.ManageItemDto;
import myshop.shop.dto.item.SearchItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static myshop.shop.entity.item.QItem.item;

public interface ItemRepositoryCustom {
    /**
     * 상품 관리 데이터 불러오기
     */
    Page<ManageItemDto> searchItemPage(Pageable pageable, SearchItemDto searchItemDto);

    /**
     * 상품 일괄 수정
     */
    long bulkItemStatusDiscount(ItemController.BulkModifyItemDto bulkModifyItemDto);
}

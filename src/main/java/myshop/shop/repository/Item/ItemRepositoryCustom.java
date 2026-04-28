package myshop.shop.repository.Item;

import myshop.shop.dto.item.ManageItemDto;
import myshop.shop.dto.item.SearchItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<ManageItemDto> searchItemPage(Pageable pageable, SearchItemDto searchItemDto);
}

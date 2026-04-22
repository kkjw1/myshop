package myshop.shop.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import myshop.shop.dto.item.AddItemDto;
import myshop.shop.dto.item.AddItemOptionDto;
import myshop.shop.entity.Seller;
import myshop.shop.entity.item.Item;
import myshop.shop.entity.item.ItemOption;
import myshop.shop.entity.item.ItemStatus;
import myshop.shop.repository.Item.ItemOptionRepository;
import myshop.shop.repository.Item.ItemRepository;
import myshop.shop.repository.seller.SellerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final SellerRepository sellerRepository;
    private final EntityManager em;

    /**
     * 상품 등록
     */
    public void saveItem(AddItemDto addItemDto) {
        Seller sellerProxy = sellerRepository.getReferenceById(addItemDto.getSellerNo());
        Item item = new Item(sellerProxy,
                addItemDto.getName(), addItemDto.getItemCategory(), addItemDto.getPrice(),
                addItemDto.getTotalStock(), addItemDto.getDiscount(), addItemDto.getMainImage(),
                addItemDto.getSubImage(), addItemDto.getContent(), ItemStatus.판매중);
        itemRepository.save(item);

        List<AddItemOptionDto> addItemOptionDtoList = addItemDto.getAddItemOptionDtoList();
        if (addItemOptionDtoList != null) {
            for (AddItemOptionDto addItemOptionDto : addItemOptionDtoList) {
                ItemOption itemOption = new ItemOption(item, addItemOptionDto.getName(), addItemOptionDto.getAdditionalPrice(), addItemOptionDto.getOptionStock());
                itemOptionRepository.save(itemOption);
            }
        }
    }
}

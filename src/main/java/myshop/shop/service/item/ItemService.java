package myshop.shop.service.item;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import myshop.shop.dto.item.AddItemDto;
import myshop.shop.dto.item.AddItemOptionDto;
import myshop.shop.dto.item.ManageItemDto;
import myshop.shop.dto.item.SearchItemDto;
import myshop.shop.entity.Seller;
import myshop.shop.entity.item.Item;
import myshop.shop.entity.item.ItemImage;
import myshop.shop.entity.item.ItemOption;
import myshop.shop.entity.item.ItemStatus;
import myshop.shop.repository.Item.ItemImageRepository;
import myshop.shop.repository.Item.ItemOptionRepository;
import myshop.shop.repository.Item.ItemRepository;
import myshop.shop.repository.seller.SellerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final SellerRepository sellerRepository;
    private final EntityManager em;
    private final ItemImageRepository itemImageRepository;

    /**
     * 상품 등록
     */
    public void saveItem(AddItemDto addItemDto) {
        Seller sellerProxy = sellerRepository.getReferenceById(addItemDto.getSellerNo());
        Item item = new Item(sellerProxy,
                addItemDto.getName(),
                addItemDto.getItemCategory(),
                addItemDto.getPrice(),
                addItemDto.getTotalStock(),
                addItemDto.getDiscount(),
                addItemDto.getContent(),
                addItemDto.getItemStatus());
        itemRepository.save(item);

        //상품 옵션 저장
        List<AddItemOptionDto> addItemOptionDtoList = addItemDto.getAddItemOptionDtoList();
        for (AddItemOptionDto addItemOptionDto : addItemOptionDtoList) {
            itemOptionRepository.save(new ItemOption(item, addItemOptionDto.getName(), addItemOptionDto.getAdditionalPrice(), addItemOptionDto.getOptionStock()));
        }

        //상품 이미지 저장
        int sortOrder = 1;
        String mainImagePath = addItemDto.getMainImagePath();
        itemImageRepository.save(new ItemImage(item, mainImagePath, true, sortOrder++));

        List<String> subImagePathList = addItemDto.getSubImagesPath();
        for (String subImagePath : subImagePathList) {
            itemImageRepository.save(new ItemImage(item, subImagePath, false, sortOrder++));
        }
    }



    /**
     * 상품 전체 조회
     */
    public List<ManageItemDto> findAllByNo(Long sellerNo) {
        Seller sellerProxy = sellerRepository.getReferenceById(sellerNo);
        List<Item> itemList = itemRepository.findBySeller(sellerProxy);
        return itemList.stream()
                .map(ManageItemDto::new)
                .collect(Collectors.toList());

    }


    /**
     * 상품 조회(페이징)
     */
    public Page<ManageItemDto> findBySearchItemDto(Pageable pageable, SearchItemDto searchItemDto) {
        return itemRepository.searchItemPage(pageable, searchItemDto);
    }



}

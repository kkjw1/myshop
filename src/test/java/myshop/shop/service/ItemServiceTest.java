package myshop.shop.service;

import myshop.shop.dto.item.AddItemDto;
import myshop.shop.dto.item.AddItemOptionDto;
import myshop.shop.entity.Seller;
import myshop.shop.entity.item.Item;
import myshop.shop.entity.item.ItemCategory;
import myshop.shop.entity.item.ItemOption;
import myshop.shop.entity.item.ItemStatus;
import myshop.shop.repository.Item.ItemOptionRepository;
import myshop.shop.repository.Item.ItemRepository;
import myshop.shop.repository.seller.SellerRepository;
import myshop.shop.service.item.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    ItemService itemService;
    @Autowired ItemRepository itemRepository;
    @Autowired SellerRepository sellerRepository;
    @Autowired ItemOptionRepository itemOptionRepository;

    @BeforeEach
    public void init() {
        Seller seller = new Seller("test", "test", "kkjjoo1212@naver.com", "판매자테스트", "010-4710-6305", "테스트회사명",
                "테스트회사전화번호", "테스트회사우편번호", "테스트회사 도로명", "테스트회사 상세주소");
        sellerRepository.save(seller);
    }


    @Test
    @DisplayName("상품 옵션 없는 경우")
    public void saveItemTest() throws Exception {
        //given
        Seller seller = sellerRepository.findById("test").orElse(null);
        AddItemDto addItemDto = new AddItemDto(seller.getNo(), "상품명", ItemCategory.상의, 10000, 10, 10, "대표이미지", "추가 이미지", "제품 상세 설명", ItemStatus.판매중);
        itemService.saveItem(addItemDto);

        List<Item> item = itemRepository.findAll();
        List<ItemOption> itemOption = itemOptionRepository.findAll();

        assertThat(item.size()).isEqualTo(1);
        assertThat(item.get(0).getName()).isEqualTo("상품명");
        assertThat(itemOption).isEmpty();


    }

    @Test
    @DisplayName("상품 옵션 있는 경우")
    public void saveItemTest2() throws Exception {
        //given
        Seller seller = sellerRepository.findById("test").orElse(null);
        AddItemOptionDto addItemOptionDto1 = new AddItemOptionDto("기본옵션1", 0, 5);
        AddItemOptionDto addItemOptionDto2 = new AddItemOptionDto("고급옵션1", 1000, 5);

        AddItemDto addItemDto = new AddItemDto(seller.getNo(), "상품명", ItemCategory.상의, 10000, 10, 10, "대표이미지", "추가 이미지", "제품 상세 설명", ItemStatus.판매중);
        addItemDto.updateAddItemOptionDtoList(addItemOptionDto1);
        addItemDto.updateAddItemOptionDtoList(addItemOptionDto2);
        itemService.saveItem(addItemDto);

        //when
        List<Item> item = itemRepository.findAll();
        List<ItemOption> itemOption = itemOptionRepository.findAll();

        //then
        assertThat(item.size()).isEqualTo(1);
        assertThat(item.get(0).getName()).isEqualTo("상품명");
        assertThat(itemOption.size()).isEqualTo(2);
        for (ItemOption option : itemOption) {
            System.out.println("option = " + option.getName());
        }
    }
}
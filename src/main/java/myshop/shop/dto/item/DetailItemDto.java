package myshop.shop.dto.item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import myshop.shop.entity.item.ItemCategory;
import myshop.shop.entity.item.ItemOption;

import java.math.BigDecimal;
import java.util.*;

@Getter @Setter
@ToString(of = {"itemNo", "itemImageMap", "itemCategory", "name", "price", "discountedPrice", "discount", "totalStock", "detailItemOptionDtoList", "content"})
public class DetailItemDto {

    private Long itemNo;

    private Map<Long, String> itemImageMap = new LinkedHashMap<>();

    private ItemCategory itemCategory;
    private String name;
    private int price;
    private BigDecimal discountedPrice;         // 할인된 가격
    private int discountPer;
    private int totalStock;

    private List<DetailItemOptionDto> detailItemOptionDtoList = new ArrayList<>();

    private String content;

    public DetailItemDto() {
    }

    public DetailItemDto(ItemCategory itemCategory, String name, int price, int discountPer, int totalStock, List<ItemOption> itemOptionList, String content) {
        this.itemCategory = itemCategory;
        this.name = name;
        this.price = price;
        this.discountPer = discountPer;
        this.totalStock = totalStock;
        this.detailItemOptionDtoList = itemOptionList.stream()
                                        .map(DetailItemOptionDto::new)
                                        .toList();
        this.content = content;
    }


}

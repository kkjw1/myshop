package myshop.shop.dto.item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import myshop.shop.entity.item.ItemCategory;
import myshop.shop.entity.item.ItemOption;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@ToString(of = {"itemImageList", "itemCategory", "name", "price", "discount", "totalStock", "detailItemOptionDtoList", "content"})
public class DetailItemDto {

    private Long itemNo;

    private List<String> itemImageList = new ArrayList<>();

    private ItemCategory itemCategory;
    private String name;
    private int price;
    private BigDecimal discountedPrice;
    private int discount;
    private int totalStock;

    private List<DetailItemOptionDto> detailItemOptionDtoList = new ArrayList<>();

    private String content;

    public DetailItemDto() {
    }

    public DetailItemDto(ItemCategory itemCategory, String name, int price, int discount, int totalStock, List<ItemOption> itemOptionList, String content) {
        this.itemCategory = itemCategory;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.totalStock = totalStock;
        this.detailItemOptionDtoList = itemOptionList.stream()
                                        .map(DetailItemOptionDto::new)
                                        .toList();
        this.content = content;
    }


}

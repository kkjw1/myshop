package myshop.shop.dto.item;

import lombok.Getter;
import lombok.Setter;
import myshop.shop.entity.item.ItemCategory;
import myshop.shop.entity.item.ItemStatus;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class AddItemDto {
    private Long sellerNo;

    private String name;
    private ItemCategory itemCategory;

    private int price;
    private int totalStock;
    private int discount;
    private List<AddItemOptionDto> addItemOptionDtoList = new ArrayList<>();

    private String mainImage;
    private String subImage;
    private String content;

    private ItemStatus itemStatus;
    private boolean useOptions;     //옵션 사용 체크박스

    public AddItemDto() {
    }

    public AddItemDto(Long sellerNo, String name, ItemCategory itemCategory, int price, int totalStock, int discount, String mainImage, String subImage, String content, ItemStatus itemStatus) {
        this.sellerNo = sellerNo;
        this.name = name;
        this.itemCategory = itemCategory;
        this.price = price;
        this.totalStock = totalStock;
        this.discount = discount;
        this.mainImage = mainImage;
        this.subImage = subImage;
        this.content = content;
        this.itemStatus = itemStatus;
    }

    public void updateAddItemOptionDtoList(AddItemOptionDto addItemOptionDto) {
        this.addItemOptionDtoList.add(addItemOptionDto);
    }
}

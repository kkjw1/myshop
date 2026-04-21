package myshop.shop.dto.item;

import lombok.Getter;
import lombok.Setter;
import myshop.shop.entity.item.ItemCategory;
import myshop.shop.entity.item.ItemStatus;

@Getter @Setter
public class AddItemDto {
    private String name;
    private ItemCategory itemCategory;

    private int price;
    private int totalStock;
    private int discount;



    private String mainImage;
    private String subImage;
    private String content;

    private ItemStatus itemStatus;



    public AddItemDto() {
    }
}

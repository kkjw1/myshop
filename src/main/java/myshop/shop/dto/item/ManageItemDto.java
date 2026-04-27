package myshop.shop.dto.item;

import lombok.Getter;
import lombok.Setter;
import myshop.shop.entity.item.Item;
import myshop.shop.entity.item.ItemStatus;

@Getter @Setter
public class ManageItemDto {

    private String mainImagePath;
    private String name;
    private int price;
    private int totalStock;
    private ItemStatus itemStatus;



    public ManageItemDto() {
    }

    public ManageItemDto(Item item) {

    }
}

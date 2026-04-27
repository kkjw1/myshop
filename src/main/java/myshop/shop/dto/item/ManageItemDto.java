package myshop.shop.dto.item;

import lombok.Getter;
import lombok.Setter;
import myshop.shop.entity.item.Item;
import myshop.shop.entity.item.ItemStatus;

import java.time.LocalDateTime;

@Getter @Setter
public class ManageItemDto {

    private String mainImagePath;
    private String name;
    private int price;
    private int totalStock;
    private ItemStatus itemStatus;
    private LocalDateTime createdDate;


    public ManageItemDto() {
    }

    public ManageItemDto(Item item) {
        this.name = item.getName();
        this.price = item.getPrice();
        this.totalStock = item.getTotalStock();
        this.itemStatus = item.getItemStatus();
        this.createdDate = item.getCreatedDate();
    }

}

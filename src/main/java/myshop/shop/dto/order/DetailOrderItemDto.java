package myshop.shop.dto.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString(of = {"itemNo", "count", "price", "totalPrice", "imageUrl", "itemName", "optionName"})
public class DetailOrderItemDto {
    private Long itemNo;
    private int count;
    private int price;
    private int totalPrice;     // count * price
    private String imageUrl;
    private String itemName;
    private String optionName;

    public DetailOrderItemDto() {
    }

    public DetailOrderItemDto(Long itemNo, int count, int price, String imageUrl, String itemName, String optionName) {
        this.itemNo = itemNo;
        this.count = count;
        this.price = price;
        this.totalPrice = count * price;
        this.imageUrl = imageUrl;
        this.itemName = itemName;
        this.optionName = optionName;
    }
}

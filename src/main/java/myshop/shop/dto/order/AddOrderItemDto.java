package myshop.shop.dto.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString(of = {"cartNo", "optionNo", "itemNo", "count", "price", "totalPrice", "imageUrl", "itemName", "optionName"})
public class AddOrderItemDto {
    private Long cartNo;
    private Long optionNo;
    private Long itemNo;
    private int count;
    private int price;
    private int totalPrice;     // count * price
    private String imageUrl;
    private String itemName;
    private String optionName;

    public AddOrderItemDto() {
    }
}

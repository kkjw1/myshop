package myshop.shop.dto.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter @Setter
@ToString(of = {"count", "price", "totalPrice", "imageUrl", "itemName", "optionName"})
public class DetailOrderItemDto {
    private int count;
    private BigDecimal price;
    private BigDecimal totalPrice;     // count * price
    private String imageUrl;
    private String itemName;
    private String optionName;

    public DetailOrderItemDto() {
    }

    public DetailOrderItemDto(int count, int price, String imageUrl, String itemName, String optionName) {
        this.count = count;
        this.price = BigDecimal.valueOf(price);
        this.totalPrice = BigDecimal.valueOf(count).multiply(BigDecimal.valueOf(price));
        this.imageUrl = imageUrl;
        this.itemName = itemName;
        this.optionName = optionName;
    }
}

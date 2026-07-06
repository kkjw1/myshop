package myshop.shop.dto.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import myshop.shop.entity.delivery.DeliveryStatus;

import java.math.BigDecimal;

@Getter @Setter
@ToString(of = {"deliveryStatus", "courier", "trackingNumber", "imageUrl", "itemName", "optionName", "totalPrice", "count"})
public class ManageOrderItemDto {
    private DeliveryStatus deliveryStatus;
    private String courier;
    private String trackingNumber;
    private String imageUrl;
    private String itemName;
    private String optionName;
    private BigDecimal totalPrice;
    private int count;

    public ManageOrderItemDto() {
    }
}

package myshop.shop.dto.item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import myshop.shop.entity.item.Item;
import myshop.shop.entity.item.ItemImage;

@Getter @Setter
@ToString(of = {"mainImagePath", "name", "price", "discount", "viewCount"})
public class MainItemDto {
    private String mainImagePath;
    private String name;
    private int price;
    private int discount;
    private Long viewCount;

    public MainItemDto() {
    }

    public MainItemDto(String mainImagePath, String name, int price, int discount, Long viewCount) {
        this.mainImagePath = mainImagePath;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.viewCount = viewCount;
    }
}

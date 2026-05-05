package myshop.shop.dto.item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import myshop.shop.entity.item.ItemOption;

@Getter @Setter
@ToString(of = {"name", "additionalPrice", "optionStock"})
public class DetailItemOptionDto {
    private String name;
    private int additionalPrice;
    private int optionStock;

    public DetailItemOptionDto() {
    }

    public DetailItemOptionDto(String name, int additionalPrice, int optionStock) {
        this.name = name;
        this.additionalPrice = additionalPrice;
        this.optionStock = optionStock;
    }

    public DetailItemOptionDto(ItemOption itemOption) {
        this.name = itemOption.getName();
        this.additionalPrice = itemOption.getAdditionalPrice();
        this.optionStock = itemOption.getOptionStock();
    }
}

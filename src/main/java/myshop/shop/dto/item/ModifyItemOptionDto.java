package myshop.shop.dto.item;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ModifyItemOptionDto {
    private String name;
    private int additionalPrice;
    private int optionStock;

    public ModifyItemOptionDto() {
    }

    public ModifyItemOptionDto(String name, int additionalPrice, int optionStock) {
        this.name = name;
        this.additionalPrice = additionalPrice;
        this.optionStock = optionStock;
    }
}

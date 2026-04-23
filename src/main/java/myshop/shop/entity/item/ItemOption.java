package myshop.shop.entity.item;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@SequenceGenerator(name = "ITEM_OPTION_SEQ", sequenceName = "ITEM_OPTION_SEQ", initialValue = 1, allocationSize = 1)
public class ItemOption {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEM_OPTION_SEQ")
    @Column(name = "ITEM_OPTION_NO")
    private Long no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_NO")
    private Item item;
    private String name;
    private int additionalPrice;
    private int optionStock;

    public ItemOption() {
    }

    public ItemOption(Item item, String name, int additionalPrice, int optionStock) {
        this.item = item;
        this.name = name;
        this.additionalPrice = additionalPrice;
        this.optionStock = optionStock;
    }
}

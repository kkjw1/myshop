package myshop.shop.entity.item;

import jakarta.persistence.*;
import lombok.Getter;
import myshop.shop.entity.BaseDateEntity;
import myshop.shop.entity.Seller;

@Entity
@Getter
@SequenceGenerator(name = "ITEM_SEQ", sequenceName = "ITEM_SEQ", initialValue = 1, allocationSize = 1)
public class Item extends BaseDateEntity {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEM_SEQ")
    @Column(name = "ITME_NO")
    private Long no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SELLER_NO")
    private Seller seller;

    private String name;
    @Enumerated(EnumType.STRING)
    private ItemCategory itemCategory;
    private int price;
    private int totalStock;
    private int discount;
    @Lob
    private String mainImage;
    @Lob
    private String subImage;
    private String content;

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    private Long viewCount;

    public Item() {
    }

    public Item(Seller seller, String name, ItemCategory itemCategory, int price, int totalStock, int discount, String mainImage, String subImage, String content, ItemStatus itemStatus) {
        this.seller = seller;
        this.name = name;
        this.itemCategory = itemCategory;
        this.price = price;
        this.totalStock = totalStock;
        this.discount = discount;
        this.mainImage = mainImage;
        this.subImage = subImage;
        this.content = content;
        this.itemStatus = itemStatus;
    }
}

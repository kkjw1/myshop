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
    private int price;
    private int discount;
    private String content;
    @Lob
    private String mainImage;
    @Lob
    private String subImage;

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;
    @Enumerated(EnumType.STRING)
    private ItemCategory itemCategory;

    private Long viewCount;

}

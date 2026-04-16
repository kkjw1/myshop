package myshop.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@SequenceGenerator(name = "ITEM_SEQ", sequenceName = "ITEM_SEQ", initialValue = 1, allocationSize = 1)
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEM_SEQ")
    @Column(name = "ITME_NO")
    private Long no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SELLER_NO")
    private Seller seller;

    private String name;
    private int price;
    private int discount;
    private String itemCategory;
    private String content;
    @Lob
    private String mainImage;
    @Lob
    private String subImage;

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    private Long viewCount;
    private String madeIn;

}

package myshop.shop.entity.item;

import jakarta.persistence.*;
import lombok.Getter;
import myshop.shop.entity.BaseDateEntity;
import myshop.shop.entity.Seller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@SequenceGenerator(name = "ITEM_SEQ", sequenceName = "ITEM_SEQ", initialValue = 1, allocationSize = 1)
public class Item extends BaseDateEntity {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEM_SEQ")
    @Column(name = "ITEM_NO")
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
    private String content;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<ItemImage> images = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    private Long viewCount;

    public Item() {
    }

    public Item(Seller seller, String name, ItemCategory itemCategory, int price, int totalStock, int discount,  String content, ItemStatus itemStatus) {
        this.seller = seller;
        this.name = name;
        this.itemCategory = itemCategory;
        this.price = price;
        this.totalStock = totalStock;
        this.discount = discount;
        this.content = content;
        this.itemStatus = itemStatus;
        this.viewCount = 0L;
    }

    //===================편의 메서드===================

    public void addImage(ItemImage image) {
        images.add(image);
        image.updateItem(this);
    }

    public Optional<ItemImage> getMainImage() {
        return images.stream()
                .filter(ItemImage::isMain)
                .findFirst();
    }
}

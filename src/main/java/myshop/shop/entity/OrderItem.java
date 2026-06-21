package myshop.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import myshop.shop.entity.delivery.Delivery;
import myshop.shop.entity.item.Item;
import myshop.shop.entity.order.Order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SequenceGenerator(name = "ORDER_ITEM_SEQ", sequenceName = "ORDER_ITEM_SEQ", initialValue = 1, allocationSize = 1)
public class OrderItem extends BaseDateEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDER_ITEM_SEQ")
    @Column(name = "ORDER_ITEM_NO")
    private Long no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_NO")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_NO")
    private Item item;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DELIVERY_NO")
    private Delivery delivery;

    private int count;
    private BigDecimal price;              // 가격 = (할인된가격 + 옵션가격) * 개수
    private String imageUrl;
    private String itemName;
    private String optionName;

    public OrderItem() {
    }

    public OrderItem(Order order, Item item, Delivery delivery, int count, BigDecimal price, String imageUrl, String itemName, String optionName) {
        this.order = order;
        this.item = item;
        this.delivery = delivery;
        this.count = count;
        this.price = price;
        this.imageUrl = imageUrl;
        this.itemName = itemName;
        this.optionName = optionName;
    }
    public OrderItem(Order order, Item item, Delivery delivery, int count, int price, String imageUrl, String itemName, String optionName) {
        this.order = order;
        this.item = item;
        this.delivery = delivery;
        this.count = count;
        this.price = BigDecimal.valueOf(price);
        this.imageUrl = imageUrl;
        this.itemName = itemName;
        this.optionName = optionName;
    }

    //==========편의 메서드===========
    public void updateOrder(Order order) {
        this.order = order;
        order.getOrderItemList().add(this);
    }

    public void updateItem(Item item) {
        this.item = item;
        item.getOrderItemList().add(this);
    }
}

package myshop.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import myshop.shop.entity.item.Item;
import myshop.shop.entity.order.Order;

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

    private int count;
    private int price;              // 개당 가격
    private int totalPrice;     // 주문한 상품 가격
    private String imageUrl;
    private String itemName;
    private String optionName;

    public OrderItem() {
    }

    public OrderItem(Order order, Item item, int count, int price, int totalPrice, String imageUrl, String itemName, String optionName) {
        this.order = order;
        this.item = item;
        this.count = count;
        this.price = price;
        this.totalPrice = totalPrice;
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

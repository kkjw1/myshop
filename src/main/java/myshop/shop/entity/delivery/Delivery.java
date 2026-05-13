package myshop.shop.entity.delivery;

import jakarta.persistence.*;
import lombok.Getter;
import myshop.shop.entity.BaseDateEntity;
import myshop.shop.entity.item.Item;
import myshop.shop.entity.order.Order;

@Entity
@Getter
@SequenceGenerator(name = "DELIVERY_SEQ", sequenceName = "DELIVERY_SEQ", initialValue = 1, allocationSize = 1)
public class Delivery extends BaseDateEntity {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DELIVERY_SEQ")
    @Column(name = "DELIVERY_NO")
    private Long no;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_NO")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_NO")
    private Item item;

    private String courier;
    private String trackingNumber;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    private String recipientName;
    private String recipientPhone;

    private int deliveryFee;


}

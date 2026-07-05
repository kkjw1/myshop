package myshop.shop.entity.delivery;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import myshop.shop.entity.BaseDateEntity;
import myshop.shop.entity.OrderItem;
import myshop.shop.entity.item.Item;
import myshop.shop.entity.order.Order;

@Entity
@Getter
@SequenceGenerator(name = "DELIVERY_SEQ", sequenceName = "DELIVERY_SEQ", initialValue = 1, allocationSize = 1)
public class Delivery extends BaseDateEntity {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DELIVERY_SEQ")
    @Column(name = "DELIVERY_NO")
    private Long no;

    private String courier;
    private String trackingNumber;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;


    @OneToOne(mappedBy = "delivery", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private OrderItem orderItem;

    public Delivery() {
    }

    public Delivery(String courier, String trackingNumber, DeliveryStatus deliveryStatus) {
        this.courier = courier;
        this.trackingNumber = trackingNumber;
        this.deliveryStatus = deliveryStatus;
    }
}

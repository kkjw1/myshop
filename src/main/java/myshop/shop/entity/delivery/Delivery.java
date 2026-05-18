package myshop.shop.entity.delivery;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
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

    private String courier;
    private String trackingNumber;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    private String recipientName;
    private String recipientPhone;
    private String postcode;
    private String roadAddress;
    private String detailAddress;

    private int deliveryFee;
    private String deliveryRequest;

    public Delivery() {
    }

    public Delivery(String courier, String trackingNumber, DeliveryStatus deliveryStatus, String recipientName, String recipientPhone,
                    String postcode, String roadAddress, String detailAddress, int deliveryFee, String deliveryRequest) {
        this.courier = courier;
        this.trackingNumber = trackingNumber;
        this.deliveryStatus = deliveryStatus;
        this.recipientName = recipientName;
        this.recipientPhone = recipientPhone;
        this.postcode = postcode;
        this.roadAddress = roadAddress;
        this.detailAddress = detailAddress;
        this.deliveryFee = deliveryFee;
        this.deliveryRequest = deliveryRequest;
    }
}

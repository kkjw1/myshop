package myshop.shop.entity.order;

import jakarta.persistence.*;
import lombok.Getter;
import myshop.shop.entity.BaseDateEntity;
import myshop.shop.entity.OrderItem;
import myshop.shop.entity.delivery.Delivery;
import myshop.shop.entity.item.Item;
import myshop.shop.entity.member.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "ORDERS")
@SequenceGenerator(name = "ORDERS_SEQ", sequenceName = "ORDERS_SEQ", initialValue = 1, allocationSize = 1)
public class Order extends BaseDateEntity {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDERS_SEQ")
    @Column(name = "ORDER_NO")
    private Long no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_NO")
    private Member member;

    private OrderStatus orderStatus;
    private int totalOrderPrice;        // 총 결제 금액
    private String postcode;
    private String roadAddress;
    private String detailAddress;


    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItemList = new ArrayList<>();

/*    @OneToOne(mappedBy = "order")
    private Delivery delivery;*/

    public Order() {
    }

    public Order(Member member, OrderStatus orderStatus, int totalOrderPrice, String postcode, String roadAddress, String detailAddress) {
        this.member = member;
        this.orderStatus = orderStatus;
        this.totalOrderPrice = totalOrderPrice;
        this.postcode = postcode;
        this.roadAddress = roadAddress;
        this.detailAddress = detailAddress;
    }
}

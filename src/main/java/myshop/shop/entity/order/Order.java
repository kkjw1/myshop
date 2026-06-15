package myshop.shop.entity.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import myshop.shop.entity.BaseDateEntity;
import myshop.shop.entity.OrderItem;
import myshop.shop.entity.delivery.Delivery;
import myshop.shop.entity.item.Item;
import myshop.shop.entity.member.Member;

import java.math.BigDecimal;
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
    private BigDecimal totalPrice;        // 총 결제 금액: orderItemList의 price합

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<OrderItem> orderItemList = new ArrayList<>();




    public Order() {
    }

    public Order(Member member, OrderStatus orderStatus, int totalPrice) {
        this.member = member;
        this.orderStatus = orderStatus;
        this.totalPrice = BigDecimal.valueOf(totalPrice);
    }
    public Order(Member member, OrderStatus orderStatus, BigDecimal totalPrice) {
        this.member = member;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
    }


    //==========편의 메서드 ============
    public void setTotalPrice(BigDecimal totalPrice) {
        for (OrderItem orderItem : orderItemList) {
            this.totalPrice.add(orderItem.getPrice());
        }
    }
}

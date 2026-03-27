package myshop.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Address {
    @Id @GeneratedValue
    @Column(name = "address_no")
    private Long no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    private String addressName;
    private String recipientName;
    private String phoneNumber;
    private String postcode;
    private String roadAddress;
    private String detailAddress;
    private String deliveryRequest;
    private Boolean mainAddress;

    protected Address() {
    }
}

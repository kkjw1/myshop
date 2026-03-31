package myshop.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@SequenceGenerator(name = "ADDRESS_SEQ", sequenceName = "ADDRESS_SEQ", initialValue = 1, allocationSize = 1)
public class Address {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADDRESS_SEQ")
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


    public Address() {
    }

    public Address(Member member, String addressName, String recipientName, String phoneNumber, String postcode, String roadAddress, String detailAddress, String deliveryRequest, Boolean mainAddress) {
        this.member = member;
        this.addressName = addressName;
        this.recipientName = recipientName;
        this.phoneNumber = phoneNumber;
        this.postcode = postcode;
        this.roadAddress = roadAddress;
        this.detailAddress = detailAddress;
        this.deliveryRequest = deliveryRequest;
        this.mainAddress = mainAddress;
    }

    public void updateAddressName(String addressName) {
        this.addressName = addressName;
    }

    public void updateRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void updatePostcode(String postcode) {
        this.postcode = postcode;
    }

    public void updateRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }

    public void updateDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public void updateDeliveryRequest(String deliveryRequest) {
        this.deliveryRequest = deliveryRequest;
    }

    public void updateMainAddress(Boolean mainAddress) {
        this.mainAddress = mainAddress;
    }
}

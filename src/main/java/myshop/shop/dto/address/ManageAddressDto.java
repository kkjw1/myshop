package myshop.shop.dto.address;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import myshop.shop.entity.Address;

@Getter @Setter
@ToString(of = {"addressName", "recipientName", "phoneNumber", "postcode", "roadAddress", "detailAddress", "deliveryRequest", "mainAddress"})
public class ManageAddressDto {
    private Long addressNo;
    private String addressName;
    private String recipientName;
    private String phoneNumber;
    private String postcode;
    private String roadAddress;
    private String detailAddress;
    private String deliveryRequest;
    private Boolean mainAddress;

    public ManageAddressDto() {
    }

    public ManageAddressDto(Long addressNo, String addressName, String recipientName, String phoneNumber,
                            String postcode, String roadAddress, String detailAddress, String deliveryRequest, Boolean mainAddress) {
        this.addressNo = addressNo;
        this.addressName = addressName;
        this.recipientName = recipientName;
        this.phoneNumber = phoneNumber;
        this.postcode = postcode;
        this.roadAddress = roadAddress;
        this.detailAddress = detailAddress;
        this.deliveryRequest = deliveryRequest;
        this.mainAddress = mainAddress;
    }

    public ManageAddressDto(Address address) {
        this.addressNo = address.getNo();
        this.addressName = address.getAddressName();
        this.recipientName = address.getRecipientName();
        this.phoneNumber = address.getPhoneNumber();
        this.postcode = address.getPostcode();
        this.roadAddress = address.getRoadAddress();
        this.detailAddress = address.getDetailAddress();
        this.deliveryRequest = address.getDeliveryRequest();
        this.mainAddress = address.getMainAddress();
    }
}

package myshop.shop.dto.Address;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import myshop.shop.entity.Address;

@Getter @Setter
@ToString(of = {"addressName", "recipientName", "phoneNumber", "postcode", "roadAddress", "detailAddress", "deliveryRequest", "mainAddress"})
public class ManageAddressDto {
    private Long no;
    private String addressName;
    @NotBlank(message = "수령인 이름이 비어있습니다.")
    private String recipientName;
    @NotBlank(message = "휴대폰 번호가 비어있습니다.")
    private String phoneNumber;
    @NotBlank(message = "주소가 비어있습니다.")
    private String postcode;
    @NotBlank(message = "주소가 비어있습니다.")
    private String roadAddress;
    private String detailAddress;
    private String deliveryRequest;
    private Boolean mainAddress;

    public ManageAddressDto() {
    }

    public ManageAddressDto(Long no, String addressName, String recipientName, String phoneNumber,
                            String postcode, String roadAddress, String detailAddress, String deliveryRequest, Boolean mainAddress) {
        this.no = no;
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
        this.no = address.getNo();
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

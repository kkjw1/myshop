package myshop.shop.dto.returnRequest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import myshop.shop.entity.ReasonCode;
import myshop.shop.entity.returnRequest.ReturnRequestStatus;

import java.math.BigDecimal;

@Getter @Setter
@ToString
public class SaveReturnRequestDto {
    private Long orderItemNo;
    private Long memberNo;
    private int count;
    private ReasonCode reasonCode;
    private String reasonDetail;
    private BigDecimal price;
    private ReturnRequestStatus returnRequestStatus;

    public SaveReturnRequestDto() {
    }
}

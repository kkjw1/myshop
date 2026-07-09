package myshop.shop.dto.cancelRequest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import myshop.shop.entity.ReasonCode;
import myshop.shop.entity.cancelRequest.CancelRequestStatus;

import java.math.BigDecimal;

@Getter @Setter
@ToString(of = {"orderNo", "orderItemNo", "memberNo", "count", "reasonCode", "reasonDetail", "price", "cancelRequestStatus"})
public class SaveCancelRequestDto {
    private Long orderNo;
    private Long orderItemNo;
    private Long memberNo;
    private int count;
    private ReasonCode reasonCode;
    private String reasonDetail;
    private BigDecimal price;
    private CancelRequestStatus cancelRequestStatus;

    public SaveCancelRequestDto() {
    }
}

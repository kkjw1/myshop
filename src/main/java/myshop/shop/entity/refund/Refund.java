package myshop.shop.entity.refund;


import jakarta.persistence.*;
import lombok.Getter;
import myshop.shop.entity.BaseDateEntity;
import myshop.shop.entity.cancelRequest.CancelRequest;
import myshop.shop.entity.returnRequest.ReturnRequest;

import java.math.BigDecimal;

@Entity
@Getter
@SequenceGenerator(name = "REFUND_SEQ", sequenceName = "REFUND_SEQ", initialValue = 1, allocationSize = 1)
public class Refund extends BaseDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REFUND_SEQ")
    @Column(name = "ITEM_NO")
    private Long no;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CANCEL_REQUEST_NO")
    private CancelRequest cancelRequest;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RETURN_REQUEST_NO")
    private ReturnRequest returnRequest;

    private BigDecimal totalPrice;          //사유코드에 따라서 배송비 추가유무 결정

    @Enumerated(EnumType.STRING)
    private RefundStatus refundStatus;

    public Refund() {
    }
}

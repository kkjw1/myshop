package myshop.shop.dto.cart;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(of = {"itemNo", "memberNo", "itemOptionNo", "itemImageNo", "count"})
public class SaveCartDto {
    private Long itemNo;
    private Long memberNo;
    private Long itemOptionNo;
    private Long itemImageNo;
    private int count;

    public SaveCartDto() {
    }

    public SaveCartDto(Long itemNo, Long memberNo, int count) {
        this.itemNo = itemNo;
        this.memberNo = memberNo;
        this.count = count;
    }

    public SaveCartDto(Long itemNo, Long memberNo, Long itemOptionNo, Long itemImageNo, int count) {
        this.itemNo = itemNo;
        this.memberNo = memberNo;
        this.itemOptionNo = itemOptionNo;
        this.itemImageNo = itemImageNo;
        this.count = count;
    }
}
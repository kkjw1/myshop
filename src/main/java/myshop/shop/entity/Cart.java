package myshop.shop.entity;


import jakarta.persistence.*;
import lombok.Getter;
import myshop.shop.entity.item.Item;
import myshop.shop.entity.member.Member;

@Entity
@Getter
@SequenceGenerator(name = "CART_SEQ", sequenceName = "CART_SEQ", initialValue = 1, allocationSize = 1)
public class Cart extends BaseDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CART_SEQ")
    @Column(name = "CART_NO")
    private Long no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_NO")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_NO")
    private Item item;

    private int count;
    private int totalPrice;

    public Cart() {
    }


}

package myshop.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_no")
    private Long no;
    private String id;
    private String password;
    private String name;
    private String telecom;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private MemberLevel level;

    @OneToMany(mappedBy = "member")
    private List<Address> addresses = new ArrayList<>();

    protected Member() {
    }
}

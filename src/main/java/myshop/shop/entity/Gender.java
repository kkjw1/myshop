package myshop.shop.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum Gender {
    MAN("남성"), WOMAN("여성"), NONE("선택안함");

    private final String val;
    Gender(String val) {
        this.val = val;
    }
}
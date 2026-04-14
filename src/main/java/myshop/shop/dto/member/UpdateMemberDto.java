package myshop.shop.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import myshop.shop.entity.Gender;
import myshop.shop.entity.Member;

@Getter @Setter
public class UpdateMemberDto {
    public String id;

    @NotBlank(message = "이름이 공백입니다.")
    private String name;

    @Email(message = "이메일 주소가 정확한지 확인해 주세요.")
    private String email;

    private String password;
    private String checkPassword;

    @NotNull(message = "통신사가 공백입니다.")
    private String telecom;
    @NotNull(message = "휴대전화번호가 공백입니다.")
    private String phoneNumber;

    private Gender gender;

    public UpdateMemberDto() {
    }

    public UpdateMemberDto(String id, String name, String email, String password, String telecom, String phoneNumber, Gender gender) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.telecom = telecom;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public UpdateMemberDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.telecom = member.getTelecom();
        this.phoneNumber = member.getPhoneNumber();
        this.gender = member.getGender();
    }



}

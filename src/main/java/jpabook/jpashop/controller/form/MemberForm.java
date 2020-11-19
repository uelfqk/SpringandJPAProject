package jpabook.jpashop.controller.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberForm {

    @NotEmpty(message = "회원이름은 필수 입니다.")
    private String name;

    private String city;
    private String street;
    private String zipcode;

    public static MemberForm createForm(String name, String city, String street, String zipcode) {
        MemberForm form = new MemberForm();
        form.setName(name);
        form.setCity(city);
        form.setStreet(street);
        form.setZipcode(zipcode);
        return form;
    }

    @Override
    public String toString() {
        return "MemberForm{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", zipcode='" + zipcode + '\'' +
                '}';
    }
}

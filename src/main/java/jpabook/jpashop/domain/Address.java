package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable //내장타입을 정의해주기 위해 Embeddable 어노테이션으로 명시해주고, 해당 타입을 쓰는 객체에는 @Embedded를 사용.
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    protected Address(){}

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}

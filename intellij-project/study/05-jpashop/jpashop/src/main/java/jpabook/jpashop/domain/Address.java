package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address() {
        // JPA 스펙상 엔티티나 임베디드 타입은 자바 기본 생성자를 public or protected로 설정해야함
        // -> JPA 구현 라이브러리가 객체 생성 시 리플랙션 같은 기술을 사용할 수 있도록 지원해야 하기 때문에
        //    이러한 제약 존재
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}

package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String name;

    @Embedded // 내장타입을 사용 했을 경우 Embedded 어노테이션 사용
    private Address address;

    @OneToMany(mappedBy = "member") //Order 필드에 있는 member에 의해 맵핑되었다는 의미. Order 의 Member member가 연관관계의 주인. 읽기 전용으로 처리됨.
    private List<Order> orders = new ArrayList<>();
}

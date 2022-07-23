package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    @NotEmpty
    private String name;

    @Embedded // 내장타입을 사용 했을 경우 Embedded 어노테이션 사용
    private Address address;

    @JsonIgnore //API상에서 json 데이터를 반환할 때 해당 멤버는 출력되지 않게한다.
    @OneToMany(mappedBy = "member") //Order 필드에 있는 member에 의해 맵핑되었다는 의미. Order 의 Member member가 연관관계의 주인. 읽기 전용으로 처리됨.
    private List<Order> orders = new ArrayList<>();
}

package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Delivery {
    @Id
    @GeneratedValue
    @Column(name="delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;

     // **default값이 EnumType.ORDINAL 인데 이는 enum의 int값을 반환한다.
    @Enumerated(EnumType.STRING)  // 만약 enum의 중간에 새로운 값이 들어가게 되면 원하는 값이 나오지 않아 오류를 초래함. ex) READY, xxx, COMP -> 1, 2, 3 (COMP의 값이 2에서 3으로 변경됨)
    private DeliveryStatus status; //READY, COMP

}

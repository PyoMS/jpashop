package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * XToOne (ManyToOne, OneToOne 에서의 성능 최적화)
 * Order
 * Order -> Member :
 * Order -> Delivery
 * */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("api/v1/simple-orders") //양방향 연관관계에 영향이 있다... => 둘 중 하나는 JsonIgnore 처리 해야한다.
    public List<Order> orderV1(){
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();    //이 LAZY가 강제 초기화 됨.
            order.getDelivery().getAddress();    //이 LAZY가 강제 초기화 됨.
        }

        return all;
    }


}

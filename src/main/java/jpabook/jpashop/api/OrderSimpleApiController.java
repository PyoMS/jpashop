package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * XToOne (ManyToOne, OneToOne 에서의 성능 최적화)
 * Order
 * Order -> Member :
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
/**
 * V1 - 엔티티가 직접적으로 외부로 노출되어 사용하면 안됨. ( 절대 )
 * V2 - DTO 클래스를 새로 선언하여 API 정보를 커스텀하게 출력해 줄 수 있음.
 *      => 한계) 의존성을 갖고 있는 엔티티의 정보 모두를 쿼리로 날린다 -> 성능문제....(N+1)
 *
 * **/
    private final OrderRepository orderRepository;

    @GetMapping("api/v1/simple-orders") //양방향 연관관계에 영향이 있다... => 둘 중 하나는 JsonIgnore 처리 해야한다.
    public List<Order> orderV1() {
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();    //이 LAZY가 강제 초기화 됨.
            order.getDelivery().getAddress();    //이 LAZY가 강제 초기화 됨.
        }
        return all;
    }


    @GetMapping("/api/v2/simple-orders")
    public Result ordersV2() {
        /**
         * N+1 문제 발생.
         * orders의 결과로 Order가 2개 생성. N=2
         * 1 + 회원 N + 배송 N
         * => 총 5개 **/
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch()); //Order객체 2개 조회됨. 쿼리는 1개 발생!(from order)

        //루프를 돌 때, 2번 수행함. -> (from member / from delivery ) 쿼리가 2번 돈다(Order객체가 2개이므로) => 쿼리 총 4개개
       List<SimpleOrderDto> simpleOrderDtoList = orders
                .stream().map(m -> new SimpleOrderDto(m))
                .collect(Collectors.toList());

        return new Result(simpleOrderDtoList.size(), simpleOrderDtoList);   /** 쿼리 수행 총 5번 발생 **/
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> simpleOrderDtoListm = orders.stream().map(m -> new SimpleOrderDto(m))
                .collect(Collectors.toList());
        return simpleOrderDtoListm;
    }

    @Data
    @AllArgsConstructor
    private static class Result<T>{
        private int count;
        private T data;
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //LAZY 초기화... 단순히 getMember()만 들고오면 null이 반환되겠지만, 그 member의 내부 변수를 호출하기 때문에 DB 조회됨.
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }
}

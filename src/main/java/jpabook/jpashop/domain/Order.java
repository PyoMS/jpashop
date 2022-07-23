package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
public class Order {
    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // FetchType.LAZY => 지연로딩, DB에서 안긁어 온다는 의미..
    @JoinColumn(name="member_id") //FK 설정하는 annotation
    private Member member;

    /**
     * Cascade 옵션이 ALL이면 Order가 persist 될 때 같이 persist 되는것.
     * 단, 연관성이 다른 곳에도 작용한다면 사용에 유의해야 함.
     * */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; //주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //Enum으로 생성. 주문상태 [ORDER , CANCEL]

    /** 양방향 연관관계 편의 메소드
     * +) 주로 이벤트가 많이 발생하는 엔터티에 설정해준다. */
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메소드==// - 별도의 생성 메서드가 있는게 좋다.
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem: orderItems) {
            order.addOrderItem(orderItem);
        }
//        Arrays.stream(orderItems).forEach(order::addOrderItem);
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비지니스 로직==//
    /**
     * 주문 취소
     * */
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP) throw new IllegalStateException("이미 배송이 완료된 상품은 취소가 불가능합니다.");

        this.setStatus(OrderStatus.CANCEL); //상태 -> cancel
        for (OrderItem orderItem : this.orderItems) {
            orderItem.cancel(); //수량 원복
        }
    }

    //==조회 로직==//
    //FIXME Stream 형으로 코드 변경할 것.
    /**
     * 전체 주문 가격 조회
     * */
    public int getTotalPrice(){ // 각각의 주문에 대한 (price * 수량) 의 총합
        return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
    }

}

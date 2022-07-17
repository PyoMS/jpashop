package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderItem {
    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; //주문 가격
    private int count; // 주문 수량

    protected OrderItem(){

    }


    //==생성 메서드==//
    /**
     * ()을 ()얼마에 () 몇 개 샀어.
     * */
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){  //Q) item에 가격이 있는데 굳이 '주문가격'을 따로 가져가는 이유는?? '할인'등의 이유로 가격이 변동될 수 있기 때문.
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);    // **** 재고에 대한 item을 주문된 수량 만큼 remove 해줘야 한다! ****
        return orderItem;
    }

    //==비지니스 로직==//
    /**
     * 주문 상품 전체 가격 조회
     * */
    public void cancel() {
        getItem().addStock(count); //FIXME this.item vs getItem() 차이점;  getItem()은 @getter 어노테이션에서 지원해주는 메서드
    }

    //==조회 로직==//
    public int getTotalPrice() {
        return this.orderPrice * this.count;
    }
}

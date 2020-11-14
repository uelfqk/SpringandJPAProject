package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int orderPrice; // 주문 당시 가격

    private int count; // 주문 당시 수량

    //==비즈니스 로직==//
    /*
    * OrderItem 의 cancel 의 의미는 아이템의 수량을 증가시키는것
    * */
    public void cancel() {
        getItem().addStock(count);
    }

    public int getTotalPrice() {
        return orderPrice * count;
    }

    //==생성 메소드==//
    /*
    * OrderItem 생성 메소드
    * OrderItem 을 생성하고 아이템 정보, 주문 가격, 수량을 입력
    * orderPrice 를 따로 받는 이유는 할인등의 이유로 가격이 변경 될 수 있기 때문에
    * 별도로 받는것이 좋다.
    * 아이템의 수량을 입력받는 수량만큼 재고 감소소
   * */
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);

        return orderItem;
    }
}

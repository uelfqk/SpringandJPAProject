package jpabook.jpashop.repository;

import jpabook.jpashop.domain.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {
    
    private String memberName; // 회원의 이름
    private OrderStatus orderStatus; // 주문 상태
}

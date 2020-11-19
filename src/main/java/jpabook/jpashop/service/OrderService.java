package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.enums.DeliveryStatus;
import jpabook.jpashop.domain.enums.OrderStatus;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /*
    * 주문 생성
    * */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송지 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        // cascade 옵션으로 OrderItem 과 Delivery 데이터는 따로 Persist 하지 않아도
        // 주문을 생성하면서 persist 를 호출하면서 OrderItem 과 Delivery 에 persist 를 전이 한다.
        // persist 하는 라이프 사이클에 대해서 동일하게 관리를 할때
        // 다른곳에서 참조할 수 없는 private 경우
        // Ex). Delivery 가 매우 중요해서 다른곳에서도 참조를 하면 cascade 를 사용하면 안된다.
        // DeliveryRepository 를 만들어서 save 하는것이 맞다.
        // 잘 이해가 되지 않으면 사용하지 않는것을 추천하며
        // 이후 코드 리펙토링을 진행하는것이 옳다.
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);

        return order.getId();
    }
    /*
    * 주문 취소
    * */
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 엔티티에 만들어준 비즈니스 로직 실행 (도메인 모델 패턴)
        order.cancel();

        // 트랜젝션 스크립트 패턴
//        if(order.getDelivery().getStatus() == DeliveryStatus.COMP) {
//            throw new IllegalStateException("배송된 상품은 주문 취소가 불가능 합니다.");
//        }
//        order.setStatus(OrderStatus.CANCEL);
//        int result = 0;
//        for (OrderItem orderItem : order.getOrderItems()) {
//            result = orderItem.getCount() + orderItem.getItem().getStockQuantity();
//            orderItem.getItem().setStockQuantity(result);
//        }
    }

    /*
    * 조회
    * */
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllWithCriteria(orderSearch);
    }
}

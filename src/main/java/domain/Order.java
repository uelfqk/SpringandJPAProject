package domain;

import domain.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.jms.JmsProperties;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "Orders")
public class Order {

    // 회원이 주문을 한다가 아니라 주문을 할때 회원정보가 필요하다.
    // 1 대 다 단방향 연관관계로 설계하는것이 좋다.
    // 객실에서 주문을 하는게 아니라
    // 주문을 하는데 객실 정보가 필요한거다.

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문시간

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]
}

package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.enums.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;
    @Autowired ItemRepository itemRepository;

    @Test
    @Transactional
    void orderTest() {
        // given
        Address address = new Address("city", "street", "zipcode");
        Member member = new Member();
        member.setName("member1");
        member.setAddress(address);
        em.persist(member);

        Book book = new Book();
        book.setName("가나다라");
        book.setPrice(10000);
        book.setStockQuantity(100);
        book.setAuthor("나나");
        book.setIsbn("isbn");
        em.persist(book);

        int orderCount = 3;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order findOrder = orderRepository.findOne(orderId);

        Assertions.assertThat(OrderStatus.ORDER).isEqualTo(findOrder.getStatus());
        Assertions.assertThat(1).isEqualTo(findOrder.getOrderItems().size());
        Assertions.assertThat(10000 * orderCount).isEqualTo(findOrder.getTotalPrice());
        Assertions.assertThat(97).isEqualTo(book.getStockQuantity());
    }

    @Test
    @Transactional
    void stockOverOrder() {
        // given
        Member member = createMember();
        Book book = createBook();

        int orderCount = 101;
        // when
//        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then

        org.junit.jupiter.api.Assertions.assertThrows(NotEnoughStockException.class,
                () -> orderService.order(member.getId(), book.getId(), orderCount));

        //org.junit.jupiter.api.Assertions.fail("재고수량 초과");
    }

    @Test
    @Transactional
    void orderCancel() {
        // given
        Member member = createMember();
        Book book = createBook();

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order findOrder = orderRepository.findOne(orderId);
        Item findBook = itemRepository.findOne(book.getId());

        Assertions.assertThat(OrderStatus.CANCEL).isEqualTo(findOrder.getStatus());
        Assertions.assertThat(100).isEqualTo(findBook.getStockQuantity());
    }

    private Member createMember() {
        Address address = new Address("city", "street", "zipcode");
        Member member = new Member();
        member.setName("member1");
        member.setAddress(address);
        em.persist(member);
        return member;
    }

    private Book createBook() {
        Book book = new Book();
        book.setName("가나다라");
        book.setPrice(10000);
        book.setStockQuantity(100);
        book.setAuthor("나나");
        book.setIsbn("isbn");
        em.persist(book);
        return book;
    }
}
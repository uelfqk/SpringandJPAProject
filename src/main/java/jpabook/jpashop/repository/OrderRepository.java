package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    /*
    * 주문 저장
    * */
    public void save(Order order) {
        em.persist(order);
    }

    /*
    * 주문 단건 조회
    * */
    public Order findOne(Long orderId) {
        return em.find(Order.class, orderId);
    }

    public List<Order> findAll(OrderSearch orderSearch) {

        return em.createQuery("select o from Order o join o.member m", Order.class)
                .setMaxResults(1000)
                .getResultList();


    }

    /*
    * All Data
    * */
    public List<Order> findAllDataAll(OrderSearch orderSearch) {
        // 값이 다 있다는 가정
        // 값이 없으면 이런 쿼리가 있을 수 없다.
        return em.createQuery("select o from Order o join o.member m" +
                " where o.status = :status" +
                " and m.name like :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .setMaxResults(1000)
                .getResultList();
    }

    /*
    * JPA Create String
    * */
    public List<Order> findAllWithCreateString(OrderSearch orderSearch) {

        // 1. 동적 쿼리를 문자열로 생성하는 시나리오
        String query = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

        // 주문 상태 검색
        if(orderSearch.getOrderStatus() != null) {
            if(isFirstCondition) {
                query += " where";
                isFirstCondition = false;
            } else {
                query += " and";
            }
            query += " o.status = :status";
        }

        // 회원 이름 검색
        if(StringUtils.hasText(orderSearch.getMemberName())) {
            if(isFirstCondition) {
                query += " where";
                isFirstCondition = false;
            } else {
                query += " and";
            }
            query += "m.name like :name";
        }

        TypedQuery<Order> query1 = em.createQuery(query, Order.class)
                .setMaxResults(1000);

        if(orderSearch.getOrderStatus() != null) {
            query1 = query1.setParameter("status", orderSearch.getOrderStatus());
        }
        if(orderSearch.getMemberName() != null) {
            query1 = query1.setParameter("name", orderSearch.getMemberName());
        }

        return query1.getResultList();
    }

    /*
    * JPA Criteria
    * */
    public List<Order> findAllWithCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        // 주문 상태 검색
        if(orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        // 회원 이름 검색
        if(StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }
}

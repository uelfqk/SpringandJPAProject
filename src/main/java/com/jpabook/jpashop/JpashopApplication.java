package com.jpabook.jpashop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);

		// 회원이 주문을 하는것이 아니라
		// 주문을 하는데 회원정보가 필요한것이다.

		// 일대일 관계에서는 어디를 연관관계 주인으로 정해도 무관하지만
		// 자주 호출하는 쪽을 연관관계 주인으로 정하는것이 좋다.

		// 비즈니스상 우위에 있다고 연관관계 주인으로 정하지 말고
		// 외래키가 있는곳을 항상 연관관계 주인으로 선택해라.


		// XToOne 시리즈는 기본 fetch Type 이 EAGER 로 설정 되어있기 때문에
		// 모두 fetch Type 을 LAZY 으로 변경 해야한다.
		// 이유 : N + 1 문제 발생
		// Ex). fetch Type 이 EAGER 인 상태에서 Order 를 N개 조회할때 연관된 Member 를 모두 찾아온다.
		// List<Order> findOrders = em.createQuery("select o from Order o").getResultList();
		// 조회된 findOrders 의 개수가 100개 이면 Member 를 추가 조회하기 위해 100건의 단방 쿼리가 DB로 전송

		// SpringBoot 의 테이블 생성 전략
		// 케멀케이스 -> UnderScore 로 변경
		// .(점) -> 언더 스코어
		// 대문자 -> 소문자
		// Ex). memberId -> member_id
	}
}

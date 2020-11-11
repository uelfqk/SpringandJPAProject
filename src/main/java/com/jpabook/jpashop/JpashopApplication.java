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


	}

}

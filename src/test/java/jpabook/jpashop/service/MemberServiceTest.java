package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    void join() {
        // given
        Address address = new Address("City", "Street", "ZipCode");

        Member member = new Member();
        member.setName("member1");
        member.setAddress(address);

        // when
        Long findMemberId = memberService.join(member);

        // then
        Assertions.assertThat(member).isEqualTo(memberRepository.findOne(findMemberId));
    }

    @Test
    @Transactional
    void duplicateMemberException() {
        // given
        Address address = new Address("city", "street", "zipcode");
        Member member = new Member();
        member.setName("member1");
        member.setAddress(address);
        memberService.join(member);

        // when
        Member member1 = new Member();
        member1.setName("member1");
        member.setAddress(address);

        // then
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> memberService.join(member1));
    }
}
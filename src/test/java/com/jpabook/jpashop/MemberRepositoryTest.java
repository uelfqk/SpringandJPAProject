package com.jpabook.jpashop;

import domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void saveMemberTest() {
        // given
        Member member = new Member();
        member.setName("member1");

        Long memberId = memberRepository.save(member);

        // when
        Member findMember = memberRepository.findById(memberId);

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(memberId);
        Assertions.assertThat(findMember.getName()).isEqualTo(member.getName());
        Assertions.assertThat(findMember).isEqualTo(member);
    }
}
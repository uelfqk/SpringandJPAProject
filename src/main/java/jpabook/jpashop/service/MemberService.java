package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jpabook.jpashop.repository.MemberRepository;

import java.util.List;

// 클래스 레벨에 @Transactional 를 붙이면 public 으로 설정된 메소드들은
// 모두 Transaction 이 걸린다.
// @Transactional(readOnly = true) -> 읽기 전용 성능 최적화
//                                 -> 클래스 레벨에 @Transactional(readOnly = true) -> 메소드 레벨에 @Transactional
//                                 -> 오버라이딩되어 @Transactional(readOnly = false)가 된다.
//                                 -> default value : readOnly = false

// @RequiredArgsConstructor -> final 키워드를 가진 필드 만을 이용해서 생성자를 만들어준다.
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    // 멀티스레드 상황에서는 이렇게 처리하여도 최후의 방어책이 필요하다.
    // Database 의 name Column 을 Unique 제약조건으로 설정하는것이 좋다.
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

        // 이렇게 작성하는게 더 성능이 좋을 것이다.
//        if(findMembers.size() != 0) {
//            throw new IllegalStateException("이미 존재하는 회원입니다.");
//        }
    }

    /*
     * 회원가입
     * */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검출
        memberRepository.save(member);
        return member.getId();
    }
    /*
     * 회원 전체 조회 
     * */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    
    /*
     * 회원 Pk 로 조회
     * */
    public Member findMember(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void updateMember(Long memberId, String name) {
        Member findMember = memberRepository.findOne(memberId);
        findMember.setName(name);
    }

    // Update 는 변경성 메소드
    // 이렇게 되면 Command 와 쿼리를 분리하는것에 위배된다.
    // findOne(memberId) 를 조회하고 Member 를 반환하게 되면 Member 를 조회하는 꼴이 된다.
    // 처리 순서 : Member 조회 -> Member 변경 -> 반환 (Member 조회)
//   @Transactional
//    public Member updateMember(Long memberId, String name) {
//        Member findMember = memberRepository.findOne(memberId);
//        findMember.setName(name);
//    }
}

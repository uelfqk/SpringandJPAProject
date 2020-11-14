package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    // 스프링이 생성한 EntityManager 를 @PersistenceContext 가 붙은 필드에 주입해준다.
    // 스프링 데이터 JPA를 사용하면 @PersistenceContext -> @Autowired 로 대체 가능
    private final EntityManager em;

    // em.persist 로 영속성 컨텍스 엔티티가 저장될때
    // 영속성 컨텍스트는 Key, Value -> Key : Pk
    // DB에 들어간 시점이 아니여도 Entity 에 Pk 에도 값을 채워준다.
    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long memberId) {
        return em.find(Member.class, memberId);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}

package daouCodeApiWork.daouCodeApi.repository;

import daouCodeApiWork.daouCodeApi.domain.Member;
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
public class MemberRepository {
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }
    public void delete(Member member){
        em.remove(member);
    }

    public Member findOne(int id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        String qlString = "select m from Member m"
                + " join fetch m.dept d";
        return em.createQuery(qlString, Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

    public List<Member> findAllByDept(int id){
        String qlString = "select m from Member m"
                + " join fetch m.dept d" +
                "where d.id = :id";
        return em.createQuery(qlString
                , Member.class)
                .setParameter("id",id)
                .getResultList();
    }

    public List<Member> findAllByMember(){
        return em.createQuery(
                "select d from Dept d"
                        + " join fetch d.dept m" , Member.class).getResultList();
    }

    public List<Member> findAllByString(MemberSearch memberSearch) {
        String jpql = "select m from Member m join m.dept d";
        boolean isFirstCondition = true;
        //deptCode
        if (StringUtils.hasText(memberSearch.getDeptCode())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }
        return em.createQuery(jpql, Member.class).getResultList();
    }

    /**
     * JPA Criteria
     */
    public List<Member> findAllByCriteria(MemberSearch memberSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> cq = cb.createQuery(Member.class);
        Root<Member> o = cq.from(Member.class);
        Join<Object, Object> m = o.join("dept", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        //키워드 검색
        if (StringUtils.hasText(memberSearch.getSearchKeyword())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" + memberSearch.getSearchKeyword() + "%");
            criteria.add(name);
            Predicate deptname =
                    cb.like(m.<String>get("dept_name"), "%" + memberSearch.getSearchKeyword() + "%");
            criteria.add(deptname);
        }

        //where AND 조건 추가 (criteria size만큼)
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Member> query = em.createQuery(cq);
        return query.getResultList();
    }
}

package daouCodeApiWork.daouCodeApi.repository;

import daouCodeApiWork.daouCodeApi.domain.Dept;
import daouCodeApiWork.daouCodeApi.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DeptRepository {
    private final EntityManager em;
    public void save(Dept dept) {em.persist(dept);}
    public void delete(Dept dept){
        em.remove(dept);
    }
    public Dept findOne(String id) {
        return em.find(Dept.class, id);
    }

    public List<Dept> findByName(String name) {
        return em.createQuery("select m from Dept m where m.name = :name", Dept.class)
                .setParameter("name", name)
                .getResultList();
    }

}

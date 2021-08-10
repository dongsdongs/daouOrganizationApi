package daouCodeApiWork.daouCodeApi;

import daouCodeApiWork.daouCodeApi.domain.Dept;
import daouCodeApiWork.daouCodeApi.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class initDB {
    private final InitService initService;

    /**
     * 최초 실행 시 component 어노테이션에 의해 실행되어
     * 기초 데이터 테스트 삽입
     */
    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {
            System.out.println("Init1" + this.getClass());

            Dept dept = createDept("01",null,"Company",0,"ABC 회사");
            em.persist(dept);

            Dept dept2 = createDept("001","01","Division",1,"경영지원본부");
            em.persist(dept2);
            Dept dept3 = createDept("002","01","Division",1,"SW 개발본부");
            em.persist(dept3);

            Dept dept4 = createDept("0001","001","Department",2,"인사팀");
            em.persist(dept4);
            Dept dept5 = createDept("0002","001","Department",2,"총무팀");
            em.persist(dept5);
            Dept dept6 = createDept("0003","001","Department",2,"법무팀");
            em.persist(dept6);

            Dept dept7 = createDept("0004","002","Department",2,"플랫폼개발부");
            em.persist(dept7);
            Dept dept8 = createDept("0005","002","Department",2,"비즈서비스개발부");
            em.persist(dept8);

            Dept dept9 = createDept("00001","0004","Department",3,"비즈플랫폼팀");
            em.persist(dept9);
            Dept dept10 = createDept("00002","0004","Department",3,"비즈서비스팀");
            em.persist(dept10);
            Dept dept11 = createDept("00003","0004","Department",3,"그룹웨어개발팀");
            em.persist(dept11);

            Dept dept12 = createDept("00004","0005","Department",3,"플랫폼서비스팀");
            em.persist(dept12);
            Dept dept13 = createDept("00005","0005","Department",3,"모바일개발팀");
            em.persist(dept13);

            Member member = createMember(1,"사장1","Member", false,  dept);
            em.persist(member);

            Member member2 = createMember(2,"경영1","Member", false, dept2);
            em.persist(member2);
            Member member3 = createMember(3,"SW1","Member", false,  dept3);
            em.persist(member3);

            //인사팀
            Member member4 = createMember(4,"인사1","Member", false,  dept4);
            em.persist(member4);
            Member member5 = createMember(5,"인사2","Member", false,  dept4);
            em.persist(member5);
            Member member6 = createMember(6,"인사3","Member", false,  dept4);
            em.persist(member6);

            //총무팀
            Member member7 = createMember(7,"총무1","Member", false,  dept5);
            em.persist(member7);
            Member member8 = createMember(8,"총무2","Member", false,  dept5);
            em.persist(member8);

            //법무팀
            Member member9 = createMember(9,"법무1","Member", false,  dept6);
            em.persist(member9);
            Member member10 = createMember(10,"법무2","Member", false,  dept6);
            em.persist(member10);

            //플랫폼개발부
            Member member11 = createMember(11,"플랫폼1","Member", true,  dept7);
            em.persist(member11);

            //비즈서비스개발부
            Member member12 = createMember(12,"서비스1","Member", true,  dept8);
            em.persist(member12);

            //비즈플랫폼팀
            Member member13 = createMember(13,"플랫폼1","Member", true,  dept9);
            em.persist(member13);
            Member member14 = createMember(14,"개발1","Member", false,  dept9);
            em.persist(member14);
            Member member15 = createMember(15,"개발2","Member", false,  dept9);
            em.persist(member15);

            //비즈서비스팀
            Member member16 = createMember(16,"개발3","Member", false,  dept10);
            em.persist(member16);
            Member member17 = createMember(17,"개발4","Member", false,  dept10);
            em.persist(member17);

            //그룹웨어개발팀
            Member member18 = createMember(18,"개발5","Member", false,  dept11);
            em.persist(member18);
            Member member19 = createMember(19,"개발6","Member", false,  dept11);
            em.persist(member19);

            //플랫폼서비스팀
            Member member20 = createMember(20,"개발7","Member", false,  dept12);
            em.persist(member20);
            Member member21 = createMember(21,"개발8","Member", false,  dept12);
            em.persist(member21);

            //모바일개발팀
            Member member22 = createMember(22,"개발9","Member", false,  dept13);
            em.persist(member22);
            Member member23 = createMember(23,"개발10","Member", false, dept13);
            em.persist(member23);
        }

        private Member createMember(int id, String name, String type,boolean manager,Dept dept) {
            Member member = new Member();
            member.setId(id);
            member.setName(name);
            member.setType(type);
            member.setManager(manager);
            member.setDept(dept);
            return member;
        }

        private Dept createDept(String id, String pId, String type, int level, String name){
            Dept dept = new Dept();
            dept.setId(id);
            dept.setPId(pId);
            dept.setType(type);
            dept.setLevel(level);
            dept.setName(name);

            return dept;
        }
    }
}

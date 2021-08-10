package daouCodeApiWork.daouCodeApi.api;

import daouCodeApiWork.daouCodeApi.domain.Member;
import daouCodeApiWork.daouCodeApi.repository.MemberRepository;
import daouCodeApiWork.daouCodeApi.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class MemeberApiTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void contextLoads() {
    }

   @Test
    public void 맴버가입() throws Exception {
        Member member = new Member();
        member.setId(0001);
        member.setName("TEST NAME");

        int savedId = memberService.join(member);

        assertEquals(member, memberRepository.findOne(savedId));
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void 중복맴버예외() throws Exception {
        Member member1 = new Member();
        member1.setId(001);
        member1.setName("LEE");

        Member member2 = new Member();
        member2.setId(001);
        member2.setName("LEE");

        memberService.join(member1);
        memberService.join(member2);

        fail("해당 맴버가 있습니다.");
    }
}

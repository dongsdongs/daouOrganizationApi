package daouCodeApiWork.daouCodeApi.service;

import daouCodeApiWork.daouCodeApi.domain.Member;
import daouCodeApiWork.daouCodeApi.repository.MemberRepository;
import daouCodeApiWork.daouCodeApi.repository.MemberSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public int join(Member member) {
        validDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 맴버입니다.");
        }
    }

    /**
     * 맴버 수정
     * memberRepository 영속성 컨텍스트에서 id를 찾아와 영속성 상태의 member에 데이터를 가져와
     * member.setName, 스프링의 AOP가 동작하면서 Transactional 어노테이션의 의해서
     * 트랜잭션이 끝나는 시점에 comit이 되고 JPA는 flush 및 영속성 컨텍스트를 comit
     */
    @Transactional
    public void update(int id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }

    //맴버 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    public Member findOne(int memberId) {
        return memberRepository.findOne(memberId);
    }
    public List<Member> findAllByDept(int deptId){
        return memberRepository.findAllByDept(deptId);
    }
    public List<Member> findAllByMember(){return memberRepository.findAllByMember();
    }
    public List<Member> findAllByCriteria(MemberSearch memberSearch){
        return memberRepository.findAllByCriteria(memberSearch);
    }

    /**
     * 맴버 삭제
     */
    @Transactional
    public void delete(int id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
        memberRepository.delete(member);
    }
}

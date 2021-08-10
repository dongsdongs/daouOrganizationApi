package daouCodeApiWork.daouCodeApi.api;

import daouCodeApiWork.daouCodeApi.domain.Dept;
import daouCodeApiWork.daouCodeApi.domain.Member;
import daouCodeApiWork.daouCodeApi.repository.MemberSearch;
import daouCodeApiWork.daouCodeApi.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    //private final MemberSearch memberSearch;
    //조회
    /**
     * 조회 : 응답 값으로 엔티티가 아닌 별도의 DTO를 반환한다.
     */
    @GetMapping("/api/organizations")
    public Result members() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getId(),m.getName(), m.getDept()))
                .collect(Collectors.toList());

        return new Result(collect);
    }
    /**
     * 등록 :
     * 요청 값으로 Member 엔티티를 직접 받지않고 CreateMemberRequest DTO를 통해서 데이터를 이동
     * Member 엔티티를 직접 받게되면 프리젠테이션 계층을 위한 로직이 추가가 되며
     * 엔티티에 @NotEmpty같은 직접적인 로직이 들어간다.
     * 엔티티가 변경되면 API 스펙이 변경되기 때문에 DTO를 통해서 데이터를 이동한다.
    */
    //등록
    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    @PostMapping("/org/member")
    public CreateMemberResponse saveMember(@RequestBody CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());
        member.setType(request.getType());
        member.setManager(request.manager);

        Dept dept = new Dept();
        dept.setId(request.getD_id());
        dept.setPId(request.getD_pId());
        dept.setLevel(request.getD_level());
        dept.setName(request.getD_name());
        dept.setType(request.getD_type());

        dept.setId(request.getDept().getId());
        dept.setPId(request.getDept().getPId());
        dept.setLevel(request.getDept().getLevel());
        dept.setName(request.getDept().getName());
        dept.setType(request.getDept().getType());

        member.setDept(dept);
        int id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     *  Spring 표준 Criteria를 통한 조회 및 검색조건 조회
     */
//    @GetMapping("/api/criteria/organizations")
//    public Result members(@RequestBody SearchMemberRequest request) {
//
//        memberSearch.setDeptCode(request.getDeptCode());
//        memberSearch.setDeptOnly(request.deptOnly);
//        memberSearch.setSearchType(request.getSearchType());
//        memberSearch.setSearchKeyword(request.getSearchKeyword());
//
//        List<Member> findMembers = memberService.findAllByCriteria(memberSearch);
//        List<MemberDto> collect = findMembers.stream()
//                .map(m -> new MemberDto(m.getId(),m.getName(), m.getDept()))
//                .collect(Collectors.toList());
//
//        return new Result(collect);
//    }

    /**
     * 수정 API
     *  수정 :
     *  update를 위한 response DTO, request DTO를 별도로 만들어서 사용
     *  등록과 같은 DTO를 사용해도 무방할것 같으나 등록과 수정의 API 스펙이 다르기 때문에
     *  별도의 update DTO를 만들어 사용
     */
    @PutMapping("/org/member/{memberId}")
    public UpdateMemberResponse updateMember(@PathVariable("memberId") int id, @RequestBody UpdateMemberRequest request) {
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    /**
     * 삭제 API
     */
    @DeleteMapping("/org/member/{memberId}")
    public DeleteMemberResponse deleteMember(@PathVariable("memberId") int id, @RequestBody  DeleteMemberRequest request) {
        memberService.delete(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new DeleteMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private int id;
        private String name;
        private Dept dept;

    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private int id;
        private String name;
    }

    @Data
    static class CreateMemberRequest {
        private String name;
        private String type;
        private boolean manager;

        private String d_id;
        private String d_pId;
        private int d_level;
        private String d_name;
        private String d_type;

        private Dept dept;
    }

    @Data
    static class SearchMemberRequest{
        private String deptCode; //deptCode
        private boolean deptOnly; //deptOnly (true: 부서만, false : 부서원 포함)
        private String searchType; //searchType (dept, member)
        private String searchKeyword; //검색어
    }

    @Data
    static class CreateMemberResponse {
        private int id;

        public CreateMemberResponse(int id) {
            this.id = id;
        }
    }

    @Data
    @AllArgsConstructor
    static class DeleteMemberResponse{
        private int id;
        private String name;
    }

    @Data
    static class DeleteMemberRequest{
        private String name;
    }
}

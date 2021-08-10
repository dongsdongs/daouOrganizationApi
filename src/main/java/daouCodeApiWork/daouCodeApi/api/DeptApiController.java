package daouCodeApiWork.daouCodeApi.api;

import daouCodeApiWork.daouCodeApi.domain.Dept;
import daouCodeApiWork.daouCodeApi.service.DeptService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DeptApiController {
    private final DeptService deptService;

    //등록
    @PostMapping("/org/dept")
    public CreateDeptResponse saveDept(@RequestBody CreateDeptRequest request) {

        Dept dept = new Dept();
        dept.setId(request.getId());
        dept.setPId(request.getPId());
        dept.setLevel(request.getLevel());
        dept.setName(request.getName());
        dept.setType(request.getType());

        String id = deptService.add(dept);
        return new CreateDeptResponse(id);
    }

    /**
     * 수정 API
     */
    @PutMapping("/org/dept/{deptId}")
    public UpdateDeptResponse updateDept(@PathVariable("deptId") String id, @RequestBody UpdateDeptRequest request) {
        deptService.update(id, request.getName());
        Dept findDept = deptService.findOne(id);
        return new UpdateDeptResponse(findDept.getId(), findDept.getName());
    }

    /**
     * 삭제 API
     */
    @DeleteMapping("/org/dept/{deptId}")
    public DeleteDeptResponse deleteDept(@PathVariable("deptId") String  id, @RequestBody DeleteDeptRequest request) {
        Dept findDept = deptService.findOne(id);
        return new DeleteDeptResponse(findDept.getId(), findDept.getName());
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    static class UpdateDeptRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateDeptResponse {
        private String id;
        private String name;
    }

    @Data
    static class CreateDeptRequest {
        private String id;
        private String pId;
        private int level;
        private String name;
        private String type;
    }

    @Data
    static class CreateDeptResponse {
        private String  id;

        public CreateDeptResponse(String id) {
            this.id = id;
        }
    }

    @Data
    @AllArgsConstructor
    static class DeleteDeptResponse{
        private String id;
        private String name;
    }

    @Data
    static class DeleteDeptRequest{
        private String name;
    }
}

package daouCodeApiWork.daouCodeApi.service;

import daouCodeApiWork.daouCodeApi.domain.Dept;
import daouCodeApiWork.daouCodeApi.repository.DeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeptService {
    private final DeptRepository deptRepository;

    /**
     * 부서 추가
     */
    @Transactional
    public String add(Dept Dept) {
        validDuplicateDept(Dept);
        deptRepository.save(Dept);
        return Dept.getId();
    }

    private void validDuplicateDept(Dept dept) {
        List<Dept> findDepts = deptRepository.findByName(dept.getName());
        if (!findDepts.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 부서입니다.");
        }
    }
    public Dept findOne(String deptId) {
        return deptRepository.findOne(deptId);
    }

    /**
     * 부서 수정
     */
    @Transactional
    public void update(String id, String name) {
        Dept dept = deptRepository.findOne(id);
        dept.setName(name);
    }
    /**
     * 부서 삭제
     */
    @Transactional
    public void delete(String id, String name) {
        Dept dept = deptRepository.findOne(id);
        dept.setName(name);
        deptRepository.delete(dept);
    }
}

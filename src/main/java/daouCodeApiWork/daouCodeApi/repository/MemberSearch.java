package daouCodeApiWork.daouCodeApi.repository;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSearch {
    private String deptCode; //deptCode
    private boolean deptOnly; //deptOnly (true: 부서만, false : 부서원 포함)
    private String searchType; //searchType (dept, member)
    private String searchKeyword; //검색어
}

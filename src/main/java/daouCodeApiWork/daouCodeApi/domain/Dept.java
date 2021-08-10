package daouCodeApiWork.daouCodeApi.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Dept {
    @Id
    @Column(name = "dept_id")
    private String id;      //부서 ID
    private String pId;     //상위 부서 ID
    private int level;      //부서 LEVEL
    private String name;    //부서명
    private String type;    //부서 타입

    @OneToMany(mappedBy = "dept", cascade = CascadeType.ALL)
    private List<Member> children = new ArrayList<>();
}

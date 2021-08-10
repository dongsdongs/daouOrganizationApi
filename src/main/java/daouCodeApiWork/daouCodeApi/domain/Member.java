package daouCodeApiWork.daouCodeApi.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.io.Serializable;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Member implements Serializable {
    @Id
    @Column(name = "member_id")
    private int id;
    private String name;
    private String type;
    private boolean manager;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "dept_id")
    private Dept dept;
}

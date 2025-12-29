package models.business;

import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.DbComment;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "v1_class_group")
@DbComment("班级分组")
public class ClassGroup extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @DbComment("唯一标识")
    private Long id;

    @Column(name = "org_id")
    @DbComment("机构id")
    private Long orgId;

    @Column(name = "class_id")
    @DbComment("班级id")
    private Long classId;

    @Column (name = "teacher_id")
    @DbComment("老师id")
    private Long  teacherId;

    @Column(name =  "group_name")
    @DbComment("分组名称")
    private String groupName;

    @Column  (name = "create_time")
    @DbComment("创建时间")
    private Long createTime;

    @Column (name = "update_time")
    @DbComment("更新时间")
    private Long updateTime;

    @Transient
    private String className;


    @Transient
    private List<StudentGroup> studentGroups;

    public static Finder<String, ClassGroup> find = new Finder<>(ClassGroup.class);
}

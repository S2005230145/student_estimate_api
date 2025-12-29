package models.business;

import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.DbComment;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "v1_group_student")
@DbComment("教师分组学生")
public class StudentGroup extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @DbComment("唯一标识")
    private Long id;

    @Column(name = "group_id")
    @DbComment("分组id")
    private Long groupId;

    @Column(name = "student_id")
    @DbComment("学生id")
    private Long studentId;

    @Column(name = "create_time")
    @DbComment("创建时间")
    private Long createTime;

    @Column(name = "update_time")
    @DbComment("更新时间")
    private Long updateTime;

    public static Finder<String, StudentGroup> find = new Finder<>(StudentGroup.class);

}

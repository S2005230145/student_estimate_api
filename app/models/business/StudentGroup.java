package models.business;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.DbComment;
import jakarta.persistence.*;
import lombok.Data;
import myannotation.EscapeHtmlAuthoritySerializer;

@Data
@Entity
@Table(name = "v1_student_group")
@DbComment("学生分组")
public class StudentGroup extends Model {

    @Column(name = "org_id")
    @DbComment("机构ID")
    public long orgId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @DbComment("唯一标识")
    public long id;

    @Column(name = "student_id")
    @DbComment("学生ID")
    public long studentId;

    @Column(name = "class_id")
    @DbComment("班级ID")
    public long classId;

    @Column(name = "group_name")
    @DbComment("分组名称")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String groupName;

    @Column(name = "create_time")
    @DbComment("创建时间")
    public long createTime;

    @Column(name = "update_time")
    @DbComment("更新时间")
    public long updateTime;

    @Column(name = "status")
    @DbComment("状态")
    public int status;

    public static Finder<Long, StudentGroup> find = new Finder<>(StudentGroup.class);


}

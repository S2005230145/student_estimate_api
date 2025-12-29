package models.business;

import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.DbComment;
import jakarta.persistence.*;
import lombok.Data;
import myannotation.Translation;

import java.util.List;

@Data
@Entity
@Table(name = "v1_class_teacher_relation")
@DbComment("班级教师关系表")
public class ClassTeacherRelation extends Model {

    @Column(name = "org_id")
    @DbComment("机构ID")
    public long orgId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @DbComment("唯一标识")
    public long id;

    @Column(name = "class_id")
    @DbComment("班级ID")
    public long classId;

    @Column(name = "teacher_id")
    @DbComment("教师ID")
    public long teacherId;

    @Column(name = "subject")
    @DbComment("任教科目")
    public String subject;

    @Column(name = "is_head_teacher")
    @DbComment("是否班主任")
    public boolean isHeadTeacher;

    @Column(name = "create_time")
    @DbComment("创建时间")
    public long createTime;

    @Column(name = "update_time")
    @DbComment("更新时间")
    public long updateTime;



    public static Finder<Long, ClassTeacherRelation> find = new Finder<>(ClassTeacherRelation.class);

    /**
     * 根据班级ID查找教师关系
     */
    public static List<ClassTeacherRelation> findByClassId(Long classId) {
        return find.query()
                .where()
                .eq("class_id", classId)
                .orderBy("is_head_teacher desc, create_time asc")
                .findList();
    }

    /**
     * 根据教师ID查找班级关系
     */
    public static List<ClassTeacherRelation> findByTeacherId(Long teacherId) {
        return find.query()
                .where()
                .eq("teacher_id", teacherId)
                .orderBy("is_head_teacher desc, create_time asc")
                .findList();
    }

    /**
     * 检查教师是否在班级任教
     */
    public static boolean isTeacherInClass(Long teacherId, Long classId) {
        return find.query()
                .where()
                .eq("teacher_id", teacherId)
                .eq("class_id", classId)
                .findCount() > 0;
    }

    /**
     * 检查教师是否在班级任班主任
     */
    public static boolean isHeadTeacherInClass(Long teacherId, Long classId) {
        return find.query()
                .where()
                .eq("class_id", classId)
                .eq("teacher_id", teacherId)
                .eq("is_head_teacher", 1)
                .findCount() > 0;
    }

    /**
     * 根据班级ID查找班主任关系
     */
    public static ClassTeacherRelation findHeadTeacherByClassId(Long classId) {
        if (classId == null || classId <= 0) {
            return null;
        }

        return find.query()
                .where()
                .eq("class_id", classId)
                .eq("is_head_teacher", true)
                .setMaxRows(1)
                .findOne();
    }

}
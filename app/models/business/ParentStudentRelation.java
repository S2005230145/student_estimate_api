package models.business;

import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.DbComment;
import jakarta.persistence.*;
import lombok.Data;
import myannotation.Translation;

import java.util.List;

import static models.business.HabitRecord.MOUTH_PARENT_MAX_SCORE;

@Data
@Entity
@Table(name = "v1_parent_student_relation")
@DbComment("家长学生关系表")
public class ParentStudentRelation extends Model {

    @Column(name = "org_id")
    @DbComment("机构ID")
    public long orgId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @DbComment("唯一标识")
    public long id;

    @Column(name = "parent_id")
    @DbComment("家长ID")
    public long parentId;

    @Column(name = "student_id")
    @DbComment("学生ID")
    public long studentId;

    @Column(name = "relationship")
    @DbComment("关系类型") // 父亲/母亲/爷爷/奶奶/外公/外婆/其他
    public String relationship;

    @Column(name = "create_time")
    @DbComment("创建时间")
    public long createTime;

    @Column(name = "update_time")
    @DbComment("更新时间")
    public long updateTime;

    @Column(name = "mouth_max_limit")
    @DbComment("月习惯最大额度")
    public Double mouthMaxLimit;

    @Column(name = "mouth_remain_limit")
    @DbComment("月习惯剩余额度")
    public Double mouthRemainLimit;

    public static Finder<Long, ParentStudentRelation> find = new Finder<>(ParentStudentRelation.class);

    /**
     * 根据家长ID查找学生关系
     */
    public static List<ParentStudentRelation> findByParentId(Long parentId) {
        return find.query()
                .where()
                .eq("parent_id", parentId)
                .orderBy("create_time asc")
                .findList();
    }

    /**
     * 根据学生ID查找家长关系
     */
    public static List<ParentStudentRelation> findByStudentId(Long studentId) {
        return find.query()
                .where()
                .eq("student_id", studentId)
                .orderBy("create_time asc")
                .findList();
    }

    /**
     * 检查家长是否与学生有关系
     */
    public static boolean isParentOfStudent(Long parentId, Long studentId) {
        return find.query()
                .where()
                .eq("parent_id", parentId)
                .eq("student_id", studentId)
                .findCount() > 0;
    }

    /**
     * 根据关系类型查找
     */
    public static List<ParentStudentRelation> findByRelationship(Long studentId, String relationship) {
        return find.query()
                .where()
                .eq("student_id", studentId)
                .eq("relationship", relationship)
                .findList();
    }

    /**
     * 添加家长学生关系
     */
    public static void addRelation(Long parentId, Long studentId, String relationship,long orgId) {
        // 检查是否已存在关系
        if (isParentOfStudent(parentId, studentId)) {
            return;
        }

        ParentStudentRelation relation = new ParentStudentRelation();
        relation.parentId = parentId;
        relation.studentId = studentId;
        relation.relationship = relationship;
        relation.orgId = orgId;
        relation.mouthMaxLimit = MOUTH_PARENT_MAX_SCORE;
        relation.mouthRemainLimit = MOUTH_PARENT_MAX_SCORE;
        relation.createTime = System.currentTimeMillis();
        relation.updateTime = System.currentTimeMillis();
        relation.save();
    }

}
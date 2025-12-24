package models.business;

import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.DbComment;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "v1_badge_student_habit")
@DbComment("学生指标记录表")
public class BadgeStudentHabit extends Model {

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

    @Column(name = "badge_id")
    @DbComment("徽章ID")
    public long badgeId;

    @Column(name = "status")
    @DbComment("状态")
    public int status;

    public final static Finder<Long, BadgeStudentHabit> find = new Finder<>(BadgeStudentHabit.class);

}

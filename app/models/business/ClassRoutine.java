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
@Table(name = "v1_class_routine")
@DbComment("班级常规评比")
public class ClassRoutine  extends Model {
    public static final int TYPE_MONTHLY = 1; //  周评
    public static final int TYPE_WEEKLY = 2; //  月评

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
    
    @Column(name = "week_number")
    @DbComment("周次")
    public int weekNumber;
    
    @Column(name = "month")
    @DbComment("月份")
    public int month;

    @Column(name = "year")
    @DbComment("年份")
    public int year;
    
    @Column(name = "hygiene_score")
    @DbComment("卫生得分")
    public double hygieneScore;
    
    @Column(name = "discipline_score")
    @DbComment("纪律得分")
    public double disciplineScore;
    
    @Column(name = "exercise_score")
    @DbComment("两操得分")
    public double exerciseScore;
    
    @Column(name = "manner_score")
    @DbComment("文明礼仪得分")
    public double mannerScore;
    
    @Column(name = "reading_score")
    @DbComment("晨诵午读得分")
    public double readingScore;
    
    @Column(name = "total_score")
    @DbComment("周总分")
    public double totalScore;

    @Column(name = "evaluator_id")
    @DbComment("评分人ID")
    public long evaluatorId;

    @Column(name = "evaluator_name")
    @DbComment("评分人姓名")
    public String evaluatorName;

    @Column(name = "evaluate_type")
    @DbComment("评分类型") // 1-周评 2-月评
    public int evaluateType;

    @Column(name = "comments")
    @DbComment("评语")
    public String comments;

    @Column(name = "record_time")
    @DbComment("记录时间")
    public long recordTime;

    @Column(name = "create_time")
    @DbComment("创建时间")
    public long createTime;

    @Column(name = "update_time")
    @DbComment("更新时间")
    public long updateTime;

    @Transient
    public String className;

    public static Finder<Long, ClassRoutine> find = new Finder<>(ClassRoutine.class);

    /**
     * 计算周总分
     * 规则：各项得分相加
     */
    public void calcTotalScore() {
        this.setTotalScore( this.hygieneScore + this.disciplineScore + this.exerciseScore +
                this.mannerScore + this.readingScore);
    }

    /**
     * 同步到班级总评分
     * 规则：根据周评/月评数据更新班级的常规评比得分
     */
    public void syncToClass() {
        try {
            SchoolClass schoolClass = SchoolClass.find.byId(this.classId);
            if (schoolClass == null) {
                return;
            }

            // 计算班级的月平均常规得分
            double monthlyAverage = calculateMonthlyAverage(this.classId, this.year, this.month);

            // 更新班级的常规评比得分
            schoolClass.setRoutineScore(monthlyAverage);

            // 重新计算班级总分
            schoolClass.calculateTotalScore();
            schoolClass.update();

        } catch (Exception e) {
            throw new RuntimeException("同步到班级失败: " + e.getMessage());
        }
    }

    /**
     * 计算指定班级某月的常规评比平均分
     */
    private double calculateMonthlyAverage(long classId, int year, int month) {
        // 获取该月所有周评记录
        List<ClassRoutine> monthlyRecords = find.query()
                .where()
                .eq("class_id", classId)
                .eq("year", year)
                .eq("month", month)
                .eq("evaluate_type", 1) // 周评
                .findList();

        if (monthlyRecords.isEmpty()) {
            return 0;
        }

        // 计算月平均分
        double total = 0;
        for (ClassRoutine record : monthlyRecords) {
            total += record.getTotalScore();
        }

        return total / monthlyRecords.size();
    }

}
package models.business;

import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.DbComment;
import jakarta.persistence.*;
import lombok.Data;
import myannotation.Translation;

@Data
@Entity
@Table(name = "v1_class_routine")
@DbComment("班级常规评比")
public class ClassRoutine  extends Model {
    
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
    
    @Column(name = "record_time")
    @DbComment("记录时间")
    public long recordTime;
    
    @Column(name = "create_time")
    @DbComment("创建时间")
    public long createTime;

    public static Finder<Long, ClassRoutine> find = new Finder<>(ClassRoutine.class);

    public double calcTotalScore() {
        return this.hygieneScore + this.disciplineScore + this.exerciseScore + this.mannerScore + this.readingScore;
    }

}
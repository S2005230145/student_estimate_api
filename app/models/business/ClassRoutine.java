package models.business;

import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import myannotation.Translation;

@Data
@Entity
@Table(name = "v1_class_routine")
@Translation("班级常规评比")
public class ClassRoutine  extends Model {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Translation("唯一标识")
    public long id;
    
    @Column(name = "class_id")
    @Translation("班级ID")
    public long classId;
    
    @Column(name = "week_number")
    @Translation("周次")
    public int weekNumber;
    
    @Column(name = "month")
    @Translation("月份")
    public int month;
    
    @Column(name = "hygiene_score")
    @Translation("卫生得分")
    public double hygieneScore;
    
    @Column(name = "discipline_score")
    @Translation("纪律得分")
    public double disciplineScore;
    
    @Column(name = "exercise_score")
    @Translation("两操得分")
    public double exerciseScore;
    
    @Column(name = "manner_score")
    @Translation("文明礼仪得分")
    public double mannerScore;
    
    @Column(name = "reading_score")
    @Translation("晨诵午读得分")
    public double readingScore;
    
    @Column(name = "total_score")
    @Translation("周总分")
    public double totalScore;
    
    @Column(name = "record_time")
    @Translation("记录时间")
    public long recordTime;
    
    @Column(name = "create_time")
    @Translation("创建时间")
    public long createTime;

    public static Finder<Long, ClassRoutine> find = new Finder<>(ClassRoutine.class);

}
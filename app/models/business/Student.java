package models.business;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.DB;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.Transaction;
import io.ebean.annotation.DbComment;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import jakarta.persistence.*;
import lombok.Data;
import myannotation.EscapeHtmlAuthoritySerializer;


import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static models.business.SchoolClass.ACADEMIC_PASS;

@Data
@Entity
@Table(name = "v1_student")
@DbComment("学生")
public class Student extends Model {

    public static final int SCHEME_A = 0; // 方案A
    public static final int SCHEME_B = 1; // 方案B
    public static final double ACADEMIC_MAX_SCORE_A = 40.0; // 方案A学业满分  //方案A学业素养
    public static final double ACADEMIC_MAX_SCORE_B = 20.0; // 方案B学业满分  //方案B学业素养
    public static final double SPECIALTY_MAX_SCORE_A = 20.0; // 方案A特长满分  //方案A扩展性素养
    public static final double SPECIALTY_MAX_SCORE_B = 40.0; // 方案B特长满分  //方案B扩展性素养
    public static final double HABIT_MAX_SCORE = 40.0; // 习惯满分  //固定
    public static final double TOTAL_MAX_SCORE = 100.0; // 总分满分

    public static final double HABIT_PASS = 32.0; // 习惯及格

    //学业及格分
    //public static final double ACADEMIC_PASS = 20.0;

    @Column(name = "org_id")
    @DbComment("机构ID")
    public long orgId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @DbComment("唯一标识")
    public long id;

    @Column(name = "student_number")
    @DbComment("学号")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String studentNumber;

    @Column(name = "name")
    @DbComment("学生姓名")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String name;

    @Column(name = "class_id")
    @DbComment("班级ID")
    public long classId;

    @Column(name = "grade")
    @DbComment("年级")
    public int grade;

    @Column(name = "class")
    @DbComment("班级")
    public int classHg;

    @Column(name = "evaluation_scheme")
    @DbComment("评价方案") // 0-方案A, 1-方案B
    public int evaluationScheme;

    @Column(name = "class_average_score")
    @DbComment("班级平均分")
    public double classAverageScore;

    @Column(name = "academic_score")
    @DbComment("学业得分")
    public double academicScore;

    @Column(name = "specialty_score")
    @DbComment("特长得分")
    public double specialtyScore;

    @Column(name = "habit_score")
    @DbComment("习惯得分")
    public double habitScore;

    @Column(name = "points")
    @DbComment("积分")
    public double points;

    @Column(name = "total_score")
    @DbComment("总分")
    public double totalScore;

    @Column(name = "badges")
    @DbComment("获得徽章")
//    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String badges;

    @Column(name = "reward_rank_grade")
    @DbComment("奖项年级排名")
    public int rewardRankGrade;

    @Column(name = "reward_rank_school")
    @DbComment("奖项学校排名")
    public int rewardRankSchool;

    @Column(name = "create_time")
    @DbComment("创建时间")
    @WhenCreated
    public long createTime;

    @Column(name = "update_time")
    @DbComment("更新时间")
    @WhenModified
    public long updateTime;

    @Column(name = "mouth_habit_score")
    @DbComment("月习惯得分")
    public double mouthHabitScore;

    @Column(name = "mouth_habit_rank")
    @DbComment("月习惯排名")
    public int mouthHabitRank;

    @Transient
    public String className;

    public static Finder<Long, Student> find = new Finder<>(Student.class);

    public boolean isOverAverage() {
        return this.academicScore > this.classAverageScore;
    }

    /**
     *判断是否达到及格分
     */
    public boolean isPass() {
        return this.academicScore >= ACADEMIC_PASS;
    }


    public boolean isHighGrade() {
        return this.grade >= 3;
    }


    /**
     * 根据班级名称设置年级和班级ID
     */
    public void setClassInfo(String className) {
        if (className.contains("一年级")) this.grade = 1;
        else if (className.contains("二年级")) this.grade = 2;
        else if (className.contains("三年级")) this.grade = 3;
        else if (className.contains("四年级")) this.grade = 4;
        else if (className.contains("五年级")) this.grade = 5;
        else if (className.contains("六年级")) this.grade = 6;
        else throw new RuntimeException("无法识别的年级: " + className);

            if (className.contains("一班")) this.classId = 1L;
        else if (className.contains("二班")) this.classId = 2L;
        else if (className.contains("三班")) this.classId = 3L;
        else if (className.contains("四班")) this.classId = 4L;
        else if (className.contains("五班")) this.classId = 5L;
        else if (className.contains("六班")) this.classId = 6L;
        else this.classId = 1L;
    }

    /**
     * 获取班级名称
     */
    public String getClassName() {
        String[] gradeNames = {"", "一年级", "二年级", "三年级", "四年级", "五年级", "六年级"};
        String[] classNames = {"", "一班", "二班", "三班", "四班", "五班", "六班","七班","八班"};

        String gradeName = this.grade >= 1 && this.grade <= 8 ? gradeNames[this.grade] : "未知年级";
        String className = this.classHg >= 1 && this.classHg <= 8 ? classNames[(int)this.classHg] : "未知班级";
        return gradeName + className;
    }

    /**
     * 设置学校排名(总排名)
     */
    public void setRankRewardSchool() {
        List<Student> students = Student.find.all();
        // 累和每个学生的每个奖项的特长分(学生ID，所有奖项分)
        Map<Long, Double> studentsGradeByGroup = students
                .stream()
                .collect(Collectors.groupingBy(
                        r -> r.id,
                        Collectors.summingDouble(Student::getSpecialtyScore)
                ));
        List<Map.Entry<Long, Double>> sortedList = studentsGradeByGroup.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .toList();

        AtomicInteger rank = new AtomicInteger(1);
        for (Map.Entry<Long, Double> entry : sortedList) {
            //定位当前学生
            Student student = students.stream()
                    .filter(v1 -> v1.getId() == entry.getKey())
                    .findFirst().orElse(null);
            if(student!=null){
                student.rewardRankGrade=rank.get();
                rank.getAndIncrement();
            }
        }
        try(Transaction transaction = Student.find.db().beginTransaction()){
            DB.updateAll(students);
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 设置年级排名
     */
    public void setRankRewardGrade() {
        List<Student> students = Student.find.all();
        // 累和每个学生的每个奖项的特长分(学生ID，所有奖项分)
        Map<Long, Double> studentsGradeByGroup = students
                .stream()
                .filter(student -> student.getGrade() == this.getGrade())
                .collect(Collectors.groupingBy(
                        r -> r.id,
                        Collectors.summingDouble(Student::getSpecialtyScore)
                ));
        List<Map.Entry<Long, Double>> sortedList = studentsGradeByGroup.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .toList();

        AtomicInteger rank = new AtomicInteger(1);
        for (Map.Entry<Long, Double> entry : sortedList) {
            //定位当前学生
            Student student = students.stream()
                    .filter(v1 -> v1.getId() == entry.getKey())
                    .findFirst().orElse(null);
            if(student!=null){
                student.rewardRankGrade=rank.get();
                rank.getAndIncrement();
            }
        }
        try(Transaction transaction = Student.find.db().beginTransaction()){
            DB.updateAll(students);
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
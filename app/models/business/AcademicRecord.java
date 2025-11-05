package models.business;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import myannotation.EscapeHtmlAuthoritySerializer;
import myannotation.Translation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.ebean.config.TenantMode.DB;

@Data
@Entity
@Table(name = "v1_academic_record")
@Translation("学业成绩记录")
public class AcademicRecord extends Model {

    public static final int EXAM_MIDTERM = 0; // 期中考试
    public static final int EXAM_FINAL = 1; // 期末考试
    public static final double PASS_SCORE = 60.0; // 及格分数
    public static final int TOP_RANKING = 50; // 前50名
    public static final double BASE_SCORE = 20.0; // 保底分
    public static final double EXCELLENT_SCORE = 40.0; // 优秀分
    public static final double PROGRESS_SCORE = 30.0; // 进步分
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Translation("唯一标识")
    public long id;
    
    @Column(name = "student_id")
    @Translation("学生ID")
    public long studentId;
    
    @Column(name = "exam_type")
    @Translation("考试类型") // 0-期中, 1-期末
    public int examType;
    
    @Column(name = "chinese_score")
    @Translation("语文成绩")
    public double chineseScore;
    
    @Column(name = "math_score")
    @Translation("数学成绩")
    public double mathScore;
    
    @Column(name = "english_score")
    @Translation("英语成绩")
    public double englishScore;
    
    @Column(name = "average_score")
    @Translation("平均分")
    public double averageScore;
    
    @Column(name = "grade_ranking")
    @Translation("年级排名")
    public int gradeRanking;
    
    @Column(name = "class_ranking")
    @Translation("班级排名")
    public int classRanking;

    @Column(name = "progress_amount")
    @Translation("进步名次") // 本次排名相比上次排名的变化值（正数表示进步）
    public int progressAmount;

    @Column(name = "progress_ranking")
    @Translation("进步排名") // 按进步幅度的排名
    public int progressRanking;
    
    @Column(name = "calculated_score")
    @Translation("计算得分")
    public double calculatedScore;
    
    @Column(name = "badge_awarded")
    @Translation("授予徽章") // 星辰徽章/星火徽章
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String badgeAwarded;
    
    @Column(name = "exam_date")
    @Translation("考试时间")
    public long examDate;
    
    @Column(name = "create_time")
    @Translation("创建时间")
    public long createTime;

    @Column(name = "update_time")
    @Translation("更新时间")
    public long updateTime;

    public static Finder<Long, AcademicRecord> find = new Finder<>(AcademicRecord.class);

    /**
     * 批量计算班级排名
     * @param academicRecords 某次考试的全部成绩数据
     * @return 计算后的成绩记录列表
     */
    public static List<AcademicRecord> batchCalcClassRanking(List<AcademicRecord> academicRecords) {
        if (academicRecords == null || academicRecords.isEmpty()) {
            return academicRecords;
        }

        // 1. 按班级分组
        Map<Long, List<AcademicRecord>> classGroups = new HashMap<>();
        for (AcademicRecord record : academicRecords) {
            Student student = Student.find.byId(record.studentId);
            if (student != null) {
                classGroups.computeIfAbsent(student.classId, k -> new ArrayList<>()).add(record);
            }
        }

        // 2. 对每个班级的成绩按平均分降序排序并设置排名
        for (List<AcademicRecord> classRecords : classGroups.values()) {
            classRecords.sort((a, b) -> Double.compare(b.averageScore, a.averageScore));
            for (int i = 0; i < classRecords.size(); i++) {
                classRecords.get(i).classRanking = i + 1;
                classRecords.get(i).updateTime = System.currentTimeMillis();
            }
        }

        return academicRecords;
    }

    /**
     * 批量计算年级排名
     * @param academicRecords 某次考试的全部成绩数据
     * @return 计算后的成绩记录列表
     */
    public static List<AcademicRecord> batchCalcGradeRanking(List<AcademicRecord> academicRecords) {
        if (academicRecords == null || academicRecords.isEmpty()) {
            return academicRecords;
        }

        // 1. 按平均分降序排序
        academicRecords.sort((a, b) -> Double.compare(b.averageScore, a.averageScore));

        // 2. 设置年级排名
        for (int i = 0; i < academicRecords.size(); i++) {
            academicRecords.get(i).gradeRanking = i + 1;
            academicRecords.get(i).updateTime = System.currentTimeMillis();
        }

        return academicRecords;
    }

    /**
     * 批量计算进步排名
     * @param currentRecords 本次考试成绩
     * @param lastRecords 上次考试成绩
     * @return 计算后的成绩记录列表
     */
    public static List<AcademicRecord> batchCalcProgressData(List<AcademicRecord> currentRecords, List<AcademicRecord> lastRecords) {
        if (currentRecords == null || currentRecords.isEmpty() || lastRecords == null || lastRecords.isEmpty()) {
            return currentRecords;
        }

        // 1. 构建上次考试成绩的映射
        Map<Long, Integer> lastRankings = new HashMap<>();
        for (AcademicRecord lastRecord : lastRecords) {
            lastRankings.put(lastRecord.studentId, lastRecord.gradeRanking);
        }

        // 2. 计算进步名次（进步幅度）
        for (AcademicRecord currentRecord : currentRecords) {
            Integer lastRanking = lastRankings.get(currentRecord.studentId);
            if (lastRanking != null && currentRecord.gradeRanking > 0) {
                // 进步名次 = 上次排名 - 本次排名（正数表示进步）
                currentRecord.progressAmount = lastRanking - currentRecord.gradeRanking;
            } else {
                currentRecord.progressAmount = 0; // 无法计算进步
            }
        }

        // 3. 按进步名次降序排序，计算进步排名
        currentRecords.sort((a, b) -> Integer.compare(b.progressAmount, a.progressAmount));

        for (int i = 0; i < currentRecords.size(); i++) {
            currentRecords.get(i).progressRanking = i + 1;
            currentRecords.get(i).updateTime = System.currentTimeMillis();
        }

        return currentRecords;
    }

    /**
     * 批量计算徽章
     */
    public static  List<AcademicRecord> batchCalcBadges(List<AcademicRecord> academicRecords) {
        if (academicRecords == null || academicRecords.isEmpty()) {
            return academicRecords;
        }

        for (AcademicRecord record : academicRecords) {
            List<String> badges = new ArrayList<>();

            // 学业优秀：年级前50名获得星辰徽章
            if (record.gradeRanking > 0 && record.gradeRanking <= 50) {
                badges.add("星辰徽章");
            }

            // 进步显著：进步排名前50名获得星火徽章
            if (record.progressRanking > 0 && record.progressRanking <= 50) {
                badges.add("星火徽章");
            }

            // 同时满足学业优秀和进步显著，授予星河徽章  todo 这部分需求没细说，暂时按这个规则给星河徽章
            if (badges.contains("星辰徽章") && badges.contains("星火徽章")) {
                badges.clear();
                badges.add("星河徽章");
            }

            record.badgeAwarded = badges.isEmpty() ? null : String.join(",", badges);
            record.updateTime = System.currentTimeMillis();
        }

        return academicRecords;
    }

    /**
     * 获取上次考试成绩
     */
    private static List<AcademicRecord> getLastExamRecords(List<AcademicRecord> currentRecords) {
        if (currentRecords == null || currentRecords.isEmpty()) {
            return new ArrayList<>();
        }

        // 获取当前记录的考试类型和时间
        int currentExamType = currentRecords.get(0).examType;
        long currentExamDate = currentRecords.get(0).examDate;

        // 获取学生ID列表
        List<Long> studentIds = currentRecords.stream()
                .map(r -> r.studentId)
                .collect(Collectors.toList());

        // 获取上次考试类型（期中/期末交替）
        int lastExamType = (currentExamType == EXAM_MIDTERM) ? EXAM_FINAL : EXAM_MIDTERM;

        // 查询上次考试成绩
        return AcademicRecord.find.query()
                .where()
                .eq("exam_type", lastExamType)
                .lt("exam_date", currentExamDate)
                .in("student_id", studentIds)
                .orderBy("exam_date desc")
                .findList();
    }


    /**
     * 一键计算所有排名和徽章
     * @param currentRecords 本次考试成绩
     * @return 计算后的成绩记录列表
     */
    public static List<AcademicRecord> batchCalcAllRankingsAndBadges(List<AcademicRecord> currentRecords) {
        if (currentRecords == null || currentRecords.isEmpty()) {
            return currentRecords;
        }

        List<AcademicRecord> lastRecords = getLastExamRecords(currentRecords);

        // 1. 计算年级排名
        batchCalcGradeRanking(currentRecords);

        // 2. 计算班级排名
        batchCalcClassRanking(currentRecords);

        // 3. 计算进步排名
        if (!lastRecords.isEmpty()) {
            batchCalcProgressData(currentRecords, lastRecords);
        }

        // 4. 计算徽章
        batchCalcBadges(currentRecords);

        return currentRecords;
    }


}
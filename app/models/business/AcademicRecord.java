package models.business;

import actor.UrbanManagementTiming;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.inject.Inject;
import io.ebean.DB;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.DbComment;
import jakarta.persistence.*;
import lombok.Data;
import models.mouth.MonthlyPerformanceSnapshot;
import myannotation.EscapeHtmlAuthoritySerializer;
import utils.ValidationUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


@Data
@Entity
@Table(name = "v1_academic_record")
@DbComment("学业成绩记录")
public class AcademicRecord extends Model {

    public static final int EXAM_MIDTERM = 0; // 期中考试
    public static final int EXAM_FINAL = 1; // 期末考试
    public static final double PASS_SCORE = 60.0; // 及格分数
    public static final int TOP_RANKING = 50; // 前50名
    public static final double BASE_SCORE = 20.0; // 保底分
    public static final double EXCELLENT_SCORE = 40.0; // 优秀分
    public static final double PROGRESS_SCORE = 30.0; // 进步分

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

    @Column(name = "exam_type")
    @DbComment("考试类型") // 0-期中, 1-期末
    public int examType;

    @Column(name = "chinese_score")
    @DbComment("语文成绩")
    public double chineseScore;

    @Column(name = "math_score")
    @DbComment("数学成绩")
    public double mathScore;

    @Column(name = "english_score")
    @DbComment("英语成绩")
    public double englishScore;

    @Column(name = "average_score")
    @DbComment("平均分")
    public double averageScore;

    //语文、数学两科平均分
//    @Column(name = "chinese_math_average_score")
//    @DbComment("语文、数学两科平均分")
//    public double chineseMathAverageScore;

    @Column(name = "grade_ranking")
    @DbComment("年级排名")
    public int gradeRanking;

    //语文、数学两科年级排名
//    @Column(name = "chinese_math_grade_ranking")
//    @DbComment("语文、数学两科年级排名")
//    public int chineseMathGradeRanking;

    @Column(name = "class_ranking")
    @DbComment("班级排名")
    public int classRanking;

    @Column(name = "progress_amount")
    @DbComment("进步名次") // 本次排名相比上次排名的变化值（正数表示进步）
    public int progressAmount;

    @Column(name = "progress_ranking")
    @DbComment("进步排名") // 按进步幅度的排名
    public int progressRanking;

    @Column(name = "calculated_score")
    @DbComment("计算得分")
    public double calculatedScore;

    @Column(name = "badge_awarded")
    @DbComment("授予徽章") // 星辰徽章/星火徽章
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String badgeAwarded;

    @Column(name = "exam_date")
    @DbComment("考试时间")
    public long examDate;

    @Column(name = "create_time")
    @DbComment("创建时间")
    public long createTime;

    @Column(name = "update_time")
    @DbComment("更新时间")
    public long updateTime;

    public static Finder<Long, AcademicRecord> find = new Finder<>(AcademicRecord.class);

    @Inject
    static UrbanManagementTiming timing;

    /**
     * 一键计算所有排名、获得徽章、获得学业分，并同步到学生表
     * 按班级导入时，会重新计算同年级所有班级的年级排名
     */
    public static List<AcademicRecord> batchCalcAllRankingsAndBadgesAndStudyScore(List<AcademicRecord> currentRecords, long orgId) {
        if (currentRecords == null || currentRecords.isEmpty()) {
            return currentRecords;
        }

        // 1. 计算年级排名（返回同年级所有记录，包括数据库中已有的）
        List<AcademicRecord> allGradeRecords = batchCalcGradeRanking(currentRecords, orgId);

        // 2. 获取导入记录的学生ID集合，用于后续只处理导入的记录
        Set<Long> importedStudentIds = currentRecords.stream()
                .map(r -> r.studentId)
                .collect(Collectors.toSet());

        // 3. 从所有年级记录中筛选出导入的记录
        List<AcademicRecord> importedRecords = allGradeRecords.stream()
                .filter(r -> importedStudentIds.contains(r.studentId))
                .collect(Collectors.toList());

        // 4. 计算班级排名（返回所有更新的班级记录，并更新 allGradeRecords 中对应记录的班级排名）
        batchCalcClassRanking(allGradeRecords, importedRecords, orgId);

        List<AcademicRecord> allUpdatedRecords = allGradeRecords;

        // 5. 获取上次考试记录（用于计算进步排名和学业分）
        List<AcademicRecord> lastRecords = getLastExamRecords(importedRecords);

        // 6. 计算进步排名（需要所有年级记录来计算进步排名）
        if (!lastRecords.isEmpty()) {
            List<AcademicRecord> allLastGradeRecords = getLastExamRecords(allGradeRecords);
            if (!allLastGradeRecords.isEmpty()) {
                batchCalcProgressData(allGradeRecords, allLastGradeRecords);
            }
        }

        // 7. 计算学业分（覆盖全部年级记录，确保旧记录被重新计算）
        batchCalcStudyScore(allGradeRecords, lastRecords);

        // 8. 计算徽章（覆盖全部年级记录，确保旧记录被重新计算）
        batchCalcBadges(allGradeRecords);

        // 9. 同步到学生表（同步所有更新的记录）
        batchSyncToStudent(allUpdatedRecords);

        // 10. 同步到班级表（同步所有更新的记录）
        updateClassAverageScores(allUpdatedRecords);

        return allUpdatedRecords;
    }

    /**
     * 批量计算班级排名
     * 按班级导入时，需要重新计算该班级所有学生的排名
     */
    public static void batchCalcClassRanking(List<AcademicRecord> allGradeRecords, List<AcademicRecord> importedRecords, long orgId) {
        if (allGradeRecords == null || allGradeRecords.isEmpty()) {
            return;
        }

        // 1. 获取所有涉及的班级ID
        Set<Long> classIds = new HashSet<>();
        for (AcademicRecord record : allGradeRecords) {
            Student student = Student.find.byId(record.studentId);
            if (student != null && student.classId > 0) {
                classIds.add(student.classId);
            }
        }

        // 2. 按班级分组所有年级记录
        Map<Long, List<AcademicRecord>> classGroups = allGradeRecords.stream()
                .filter(record -> {
                    Student student = Student.find.byId(record.studentId);
                    return student != null && classIds.contains(student.classId) && student.orgId == orgId;
                })
                .collect(Collectors.groupingBy(record -> {
                    Student student = Student.find.byId(record.studentId);
                    return student.classId;
                }));

        // 3. 对每个班级的成绩按平均分降序排序并设置排名
        for (List<AcademicRecord> classRecords : classGroups.values()) {
            classRecords.sort((a, b) -> Double.compare(b.averageScore, a.averageScore));
            for (int i = 0; i < classRecords.size(); i++) {
                classRecords.get(i).classRanking = i + 1;
                classRecords.get(i).updateTime = System.currentTimeMillis();
            }
        }
    }

    /**
     * 批量计算年级排名
     * 按班级导入时，需要重新计算同年级所有班级的年级排名
     */
    public static List<AcademicRecord> batchCalcGradeRanking(List<AcademicRecord> academicRecords, long orgId) {
        if (academicRecords == null || academicRecords.isEmpty()) {
            return academicRecords;
        }

        // 1. 获取导入记录的年级、考试类型和时间
        int grade = 0;
        int examType = academicRecords.get(0).examType;
        long examDate = academicRecords.get(0).examDate;

        for (AcademicRecord record : academicRecords) {
            Student student = Student.find.byId(record.studentId);
            if (student != null) {
                grade = student.grade;
                break;
            }
        }

        if (grade == 0) {
            return academicRecords;
        }

        // 2. 查询该年级、该考试的所有记录（包括数据库中已有的）
        // 用 LinkedHashMap 去重，确保同一学生在同一场考试只保留一条记录
        Map<Long, AcademicRecord> recordMap = new LinkedHashMap<>();

        List<Student> studentsInGrade = Student.find.query()
                .where()
                .eq("grade", grade)
                .findList();

        if (!studentsInGrade.isEmpty()) {
            List<Long> studentIds = studentsInGrade.stream()
                    .map(s -> s.id)
                    .collect(Collectors.toList());

            List<AcademicRecord> gradeRecords = AcademicRecord.find.query()
                    .where()
                    .eq("exam_type", examType)
                    .eq("exam_date", examDate)
                    .eq("org_id", orgId)
                    .in("student_id", studentIds)
                    .findList();

            for (AcademicRecord r : gradeRecords) {
                recordMap.put(r.studentId, r); // 后查到的会覆盖先前重复的
            }
        }

        // 3. 合并导入的记录（更新或新增）
        for (AcademicRecord importedRecord : academicRecords) {
            AcademicRecord existing = recordMap.get(importedRecord.studentId);
            if (existing != null) {
                existing.averageScore = importedRecord.averageScore;
                existing.chineseScore = importedRecord.chineseScore;
                existing.mathScore = importedRecord.mathScore;
                existing.englishScore = importedRecord.englishScore;
                existing.orgId = orgId;
            } else {
                importedRecord.orgId = orgId;
                recordMap.put(importedRecord.studentId, importedRecord);
            }
        }

        List<AcademicRecord> allGradeRecords = new ArrayList<>(recordMap.values());

        // 4. 按平均分降序排序
        allGradeRecords.sort((a, b) -> Double.compare(b.averageScore, a.averageScore));

        // 5. 设置年级排名
        for (int i = 0; i < allGradeRecords.size(); i++) {
            System.out.println("i="+i);
            allGradeRecords.get(i).setGradeRanking(i+1);
            allGradeRecords.get(i).setUpdateTime(System.currentTimeMillis());
            allGradeRecords.get(i).markAsDirty();
        }

        return allGradeRecords;
    }

    /**
     * 批量计算进步排名
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
    public static List<AcademicRecord> batchCalcBadges(List<AcademicRecord> academicRecords) {
        if (academicRecords == null || academicRecords.isEmpty()) {
            return academicRecords;
        }

        for (AcademicRecord record : academicRecords) {
            List<String> badges = new ArrayList<>();


            //获取当前的学生的生活习惯得分
            Student student = Student.find.byId(record.studentId);

            //查询习惯monthly_performance_snapshot
            List<MonthlyPerformanceSnapshot> list = MonthlyPerformanceSnapshot.find.query()
                    .where()
                    .eq("student_id", record.studentId)
                    .eq("settle_state", 0)
                    .eq("type", "Habit").findList();

            BigDecimal habitScore = null;
            BigDecimal averageHabit = null;
            if (student != null) {
                habitScore = BigDecimal.valueOf(student.getHabitScore());
            }
            BigDecimal sumHabit = list.stream()
                    .map(MonthlyPerformanceSnapshot::getSumFinalScore)
                    .map(BigDecimal::valueOf)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .add(habitScore);

            int size = list.size();
            if (size > 0) {
                averageHabit = sumHabit.divide(BigDecimal.valueOf(size), 2, RoundingMode.HALF_UP);
                // 使用 averageHabit.doubleValue() 或保留 BigDecimal 传递
            }


            if (student != null  && averageHabit!=null && averageHabit.compareTo(new BigDecimal("32.0"))>=0) {
                // 学业优秀：年级前50名获得星辰徽章
                if(record.gradeRanking > 0 && record.gradeRanking <= TOP_RANKING){
                    badges.add("敏行徽章");
                }
                // 进步显著：进步排名前50名获得星火徽章
                if(record.progressRanking > 0 && record.progressRanking <= TOP_RANKING){
                    badges.add("力行徽章");
                }
            }

            record.badgeAwarded = badges.isEmpty() ? null : String.join(",", badges);
            record.updateTime = System.currentTimeMillis();
        }

        return academicRecords;
    }

    /**
     * 批量计算学业得分
     */
    public static List<AcademicRecord> batchCalcStudyScore(List<AcademicRecord> academicRecords, List<AcademicRecord> lastRecords) {
        if (academicRecords == null || academicRecords.isEmpty()) {
            return academicRecords;
        }

        // 构建上次考试成绩的映射，方便查找
        Map<Long, AcademicRecord> lastRecordMap = new HashMap<>();
        if (lastRecords != null && !lastRecords.isEmpty()) {
            for (AcademicRecord lastRecord : lastRecords) {
                // 如果同一个学生有多条记录，取最新的（examDate最大的）
                AcademicRecord existing = lastRecordMap.get(lastRecord.studentId);
                if (existing == null || lastRecord.examDate > existing.examDate) {
                    lastRecordMap.put(lastRecord.studentId, lastRecord);
                }
            }
        }

        for (AcademicRecord record : academicRecords) {
            AcademicRecord lastRecord = lastRecordMap.get(record.studentId);
            record.calculatedScore = calculateStudyScore(record, lastRecord);
            record.updateTime = System.currentTimeMillis();
        }

        return academicRecords;
    }

    /**
     * 计算单条记录的学业得分
     */
    private static double calculateStudyScore(AcademicRecord record, AcademicRecord lastRecord) {
        double score = 0.0;

        // 获取学生信息
        Student student = Student.find.byId(record.studentId);
        if (student == null) {
            return 0.0;
        }

        // 1. 学业优秀表彰：年级前50名，得40分
        if (student.evaluationScheme == Student.SCHEME_A) {
            if (record.gradeRanking > 0 && record.gradeRanking <= TOP_RANKING) {
                score = 40.0;
            } else if (record.averageScore >= 90) {
                score = 35.0;
            } else if (record.averageScore >= 80) {
                score = 30.0;
            } else if (record.averageScore >= 70) {
                score = 25.0;
            } else if (record.averageScore >= 60) {
                score = 20.0;
            } else {
                score = 0.0;
            }
        } else if (student.evaluationScheme == Student.SCHEME_B) {
            if (record.averageScore >= 60) {
                score = Student.ACADEMIC_MAX_SCORE_B;
            } else {
                score = 0.0;
            }
        }

        // 2. 进步表彰：满足以下条件之一即可得分，取最高值，不重复计算
        if (lastRecord != null) {
            double progressScore = 0.0;

            // a. 排名进步：进步排名前50名，得30分
            if (record.progressRanking > 0 && record.progressRanking <= TOP_RANKING) {
                progressScore = 30.0;
            }

            // b. 分数进步：平均分较上次提高10分及以上，得20分；提高5-9分，得10分
            double scoreImprovement = record.averageScore - lastRecord.averageScore;
            double scoreProgressPoints = 0.0;
            if (scoreImprovement >= 10.0) {
                scoreProgressPoints = 20.0;
            } else if (scoreImprovement >= 5.0) {
                scoreProgressPoints = 10.0;
            }

            // 取最高值（排名进步和分数进步取最高值，不重复计算）
            double maxProgressScore = Math.max(progressScore, scoreProgressPoints);

            score = Math.max(score,maxProgressScore);
        }

        return score;
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
     * 批量同步到学生表
     */
    public static void batchSyncToStudent(List<AcademicRecord> academicRecords) {
        if (academicRecords == null || academicRecords.isEmpty()) {
            return;
        }

        // 1. 批量获取所有学生ID
        List<Long> studentIds = academicRecords.stream()
                .map(r -> r.studentId)
                .distinct()
                .collect(Collectors.toList());

        // 2. 批量查询学生信息（一次查询）
        Map<Long, Student> studentMap = Student.find.query()
                .where()
                .in("id", studentIds)
                .findList()
                .stream()
                .collect(Collectors.toMap(s -> s.id, s -> s));

        // 3. 按学生ID分组成绩记录
        Map<Long, List<AcademicRecord>> studentRecordsMap = academicRecords.stream()
                .collect(Collectors.groupingBy(r -> r.studentId));

        // 4. 批量更新学生数据
        List<Student> studentsToUpdate = new ArrayList<>();

        for (Map.Entry<Long, List<AcademicRecord>> entry : studentRecordsMap.entrySet()) {
            Long studentId = entry.getKey();
            List<AcademicRecord> studentRecords = entry.getValue();

            Student student = studentMap.get(studentId);
            if (student == null) {
                System.err.println("学生ID " + studentId + " 不存在，跳过同步");
                continue;
            }

            // 获取最新的考试成绩
            AcademicRecord latestRecord = studentRecords.stream()
                    .max((a, b) -> Long.compare(a.examDate, b.examDate))
                    .orElse(null);

            if (latestRecord == null) {
                continue;
            }

            // 更新学生数据
            student.setAcademicScore(latestRecord.calculatedScore);

            if (!ValidationUtil.isEmpty(latestRecord.badgeAwarded)) {
                //写法一
                student.setBadges(latestRecord.badgeAwarded);
                // 写法二
//                 student.badges=latestRecord.badgeAwarded;
//                student.markAsDirty();
            } else {
                student.setBadges("");
            }

            studentsToUpdate.add(student);
        }

        // 5. 批量保存更新
        if (!studentsToUpdate.isEmpty()) {
            DB.updateAll(studentsToUpdate);
            System.out.println("成功同步 " + studentsToUpdate.size() + " 名学生数据");
        }
    }


    /**
     * 更新学生的班级平均分
     */
    public static void updateClassAverageScores(List<AcademicRecord> academicRecords) {
        if (academicRecords == null || academicRecords.isEmpty()) {
            return;
        }

        // 1. 批量获取所有学生信息（一次查询）
        List<Long> studentIds = academicRecords.stream()
                .map(r -> r.studentId)
                .distinct()
                .collect(Collectors.toList());


        Map<Long, Student> studentMap = Student.find.query().where()
                .in("id", studentIds)
                .findList()
                .stream()
                .collect(Collectors.toMap(s -> s.id, s -> s));


        // 2. 按班级分组计算平均分
        Map<Long, List<Double>> classScoresMap = new HashMap<>();

        for (AcademicRecord record : academicRecords) {
            Student student = studentMap.get(record.studentId);
            if (student != null) {
                classScoresMap.computeIfAbsent(student.classId, k -> new ArrayList<>())
                        .add(record.averageScore);
            }
        }

        // 3. 计算每个班级的平均分
        Map<Long, Double> classAverageMap = new HashMap<>();
        for (Map.Entry<Long, List<Double>> entry : classScoresMap.entrySet()) {
            double average = entry.getValue().stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
            classAverageMap.put(entry.getKey(), Math.round(average * 100.0) / 100.0);

        }

        // 4. 批量查询所有相关班级的学生（一次查询）
        List<Long> classIds = new ArrayList<>(classAverageMap.keySet());
        List<Student> allClassStudents = Student.find.query()
                .where()
                .in("class_id", classIds)
                .findList();

        // 5. 批量更新学生数据
        List<Student> studentsToUpdate = new ArrayList<>();
        for (Student student : allClassStudents) {
            Double classAverage = classAverageMap.get(student.classId);
            if (classAverage != null) {
                student.setClassAverageScore(classAverage);
                studentsToUpdate.add(student);
            }
        }


        // 6. 批量保存
        if (!studentsToUpdate.isEmpty()) {
            DB.saveAll(studentsToUpdate);
            System.out.println("更新 " + classIds.size() + " 个班级的平均分，涉及 " + studentsToUpdate.size() + " 名学生");
        }

        SchoolClass.batchRecalcAcademicScores(classIds);
    }
}
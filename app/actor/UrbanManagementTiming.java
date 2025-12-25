package actor;

import akka.actor.ActorSystem;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ebean.DB;
import io.ebean.Transaction;
import models.business.HabitRecord;
import models.business.SpecialtyAward;
import models.business.Student;
import models.mouth.MonthlyPerformanceSnapshot;
import play.Logger;
import play.cache.NamedCache;
import utils.IdGenerator;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
public class UrbanManagementTiming {

    private final Logger.ALogger logger = Logger.of(UrbanManagementTiming.class);

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2); // 创建一个单线程的定时执行器

    // 成员变量（放在类里）
    private akka.actor.Cancellable monthTask;

    private final ActorSystem actorSystem;

    private final double student_base_habit_record_score=15.0;

//    private final static ObjectMapper objectMapper=new ObjectMapper();

    //默认时间是最大时间  9999999999
//    private static final LocalDateTime[] startTime = {
//            LocalDateTime.of(9999, 12, 31, 23, 59, 59, 999_000_000)
//    };

    public final LocalDateTime[] startTime ={LocalDateTime.now()};

    @Inject
    @NamedCache("redis")
    protected play.cache.redis.AsyncCacheApi redis;



    @Inject
    public UrbanManagementTiming(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;



        actorSystem.scheduler().scheduleOnce(
                scala.concurrent.duration.Duration.create(0, TimeUnit.SECONDS),
                this::scheduleMonth,
                actorSystem.dispatcher()
        );



    }

    private void scheduleMonth(){
        actorSystem.scheduler().scheduleWithFixedDelay(
                scala.concurrent.duration.Duration.create(startTime[0].getSecond(), TimeUnit.SECONDS),
                scala.concurrent.duration.Duration.create(calculateSecondsToMonthEnd(startTime[0]), TimeUnit.SECONDS),
                () -> {
                    try {
                        startTime[0] = startTime[0].with(TemporalAdjusters.firstDayOfNextMonth())
                                .withHour(0)
                                .withMinute(0)
                                .withSecond(0)
                                .withNano(0);

                        calculateMonthlyAwards();
                        calculateMonthlyHabits();
                    } catch (Exception e) {
                        logger.error("MonthlyPerformanceSnapshot定时出错", e);
                    }
                },
                actorSystem.dispatcher()
        );
    }

    /**
     * 取消定时任务（供其他接口调用）
     */
    public void cancelMonthTask() {
        if (monthTask != null && !monthTask.isCancelled()) {
            monthTask.cancel();
            logger.info("monthTask 已取消");
        }
    }

    /**
     * 计算到指定时间的初始延迟（秒）
     * @param hour 目标小时
     * @param minute 目标分钟
     * @return 到目标时间的秒数
     */
    private long calculateInitialDelay(int hour, int minute) {
        LocalTime now = LocalTime.now();
        LocalTime targetTime = LocalTime.of(hour, minute);
        long initialDelay;

        if (now.isBefore(targetTime)) {
            // 今天还没到目标时间
            initialDelay = now.until(targetTime, ChronoUnit.SECONDS);
        } else {
            // 今天已经过了目标时间，计算到明天目标时间的时间
            initialDelay = now.until(targetTime, ChronoUnit.SECONDS) + 24 * 60 * 60;
        }

        return initialDelay;
    }

    public long calculateSecondsToMonthEnd(LocalDateTime now) {
        // 获取当月最后一天的最后时刻
        LocalDateTime endOfMonth = now
                .with(TemporalAdjusters.lastDayOfMonth())
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999_999_999);

        Duration duration = Duration.between(now, endOfMonth);
        return duration.getSeconds();  // 返回秒数
    }


    /**
     * 每月月底统计每个学生月习惯分数
     */
    private void calculateMonthlyHabits() {

        try {
            // 获取上个月的年月（因为是在每月第一天执行，统计的是上个月的数据）
            LocalDate today = LocalDate.now();
            YearMonth lastMonth = YearMonth.from(today).minusMonths(1);
            String year = String.valueOf(lastMonth.getYear());
            String month = String.valueOf(lastMonth.getMonthValue());

            // 计算上个月的时间范围（上个月第一天00:00:00 到 上个月最后一天23:59:59）
            LocalDateTime lastMonthStart = lastMonth.atDay(1).atStartOfDay();
            LocalDateTime lastMonthEnd = lastMonth.atEndOfMonth().atTime(23, 59, 59);
            long lastMonthStartTimestamp = lastMonthStart.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long lastMonthEndTimestamp = lastMonthEnd.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

            // 查询上个月所有审核通过的习惯记录
            List<HabitRecord> habitRecordList = HabitRecord.find.query()
                    .where()
                    .eq("status", HabitRecord.STATUS_UNSETTLED)
                    .ge("award_date", lastMonthStartTimestamp)
                    .le("award_date", lastMonthEndTimestamp)
                    .findList();

            // 按学生ID分组，统计每个学生的奖项分数总和
//            Map<Long, Double> studentHabitScores = new HashMap<>();
//            for (HabitRecord habitRecord : habitRecordList) {
//                studentHabitScores.merge(habitRecord.getStudentId(), habitRecord.scoreChange, Double::sum);
//            }

            // 获取所有学生
            List<Student> allStudents = Student.find.all();

            // 创建快照记录列表
            List<MonthlyPerformanceSnapshot> snapshots = new ArrayList<>();
            long currentTime = System.currentTimeMillis();

            //如果没有习惯记录，则返回
            if (habitRecordList.isEmpty()) return;

            // 为每个学生创建快照记录并重置specialtyScore
            for (Student student : allStudents) {
                // 获取该学生上个月的奖项分数总和（如果没有记录则为0）
                //Double monthlyAwardScore = studentHabitScores.getOrDefault(student.getId(), 0.0);

                // 创建快照记录
                MonthlyPerformanceSnapshot snapshot = new MonthlyPerformanceSnapshot();
                snapshot.setRecordId(IdGenerator.getId());
                snapshot.setStudentId(student.getId());
                snapshot.setYear(year);
                snapshot.setMouth(month);
                snapshot.setSumFinalScore(student.getHabitScore());
                snapshot.setSettleState(1L); // 设置为已结算
                snapshot.setSettleTime(currentTime);
                snapshot.setType("Habit"); // 设置类型为习惯
                snapshots.add(snapshot);

                // 重置学生的specialtyScore为0
                student.setHabitScore(HabitRecord.BASE_SCORE);
            }

            // 保存所有快照记录
            try (Transaction transaction = MonthlyPerformanceSnapshot.find.db().beginTransaction()) {
                DB.saveAll(snapshots);
                transaction.commit();
            } catch (Exception e) {
                logger.error("保存月度快照记录出错", e);
                throw e;
            }

            // 更新所有学生的specialtyScore
            try (Transaction transaction = Student.find.db().beginTransaction()) {
                DB.updateAll(allStudents);
                transaction.commit();
            } catch (Exception e) {
                logger.error("重置学生specialtyScore出错", e);
                throw e;
            }

            logger.info("成功统计并保存 {} 个学生的月度奖项分数（上个月：{}-{}）", allStudents.size(), year, month);
        } catch (Exception e) {
            logger.error("统计月度奖项分数出错", e);
        }
    }


    /**
     * 每个月第一天凌晨1:00统计上个月每个学生月奖项分数
     */
    private void calculateMonthlyAwards() {
        try {
            // 获取上个月的年月（因为是在每月第一天执行，统计的是上个月的数据）
            LocalDate today = LocalDate.now();
            YearMonth lastMonth = YearMonth.from(today).minusMonths(1);
            String year = String.valueOf(lastMonth.getYear());
            String month = String.valueOf(lastMonth.getMonthValue());

            // 计算上个月的时间范围（上个月第一天00:00:00 到 上个月最后一天23:59:59）
            LocalDateTime lastMonthStart = lastMonth.atDay(1).atStartOfDay();
            LocalDateTime lastMonthEnd = lastMonth.atEndOfMonth().atTime(23, 59, 59);
            long lastMonthStartTimestamp = lastMonthStart.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long lastMonthEndTimestamp = lastMonthEnd.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

            // 查询上个月所有审核通过的奖项记录
            List<SpecialtyAward> awards = SpecialtyAward.find.query()
                    .where()
                    .eq("status", SpecialtyAward.STATUS_APPROVED)
                    .ge("createTime", lastMonthStartTimestamp)
                    .le("createTime", lastMonthEndTimestamp)
                    .findList();

            //如果没有奖项记录，则返回
            if (awards.isEmpty()) return;

            // 按学生ID分组，统计每个学生的奖项分数总和
//            Map<Long, Double> studentAwardScores = new HashMap<>();
//            for (SpecialtyAward award : awards) {
//                studentAwardScores.merge(award.getStudentId(), award.getAwardScore(), Double::sum);
//            }

            // 获取所有学生
            List<Student> allStudents = Student.find.all();

            // 创建快照记录列表
            List<MonthlyPerformanceSnapshot> snapshots = new ArrayList<>();
            long currentTime = System.currentTimeMillis();

            // 为每个学生创建快照记录并重置specialtyScore
            for (Student student : allStudents) {
                // 获取该学生上个月的奖项分数总和（如果没有记录则为0）
                //Double monthlyAwardScore = studentAwardScores.getOrDefault(student.getId(), 0.0);

                // 创建快照记录
                MonthlyPerformanceSnapshot snapshot = new MonthlyPerformanceSnapshot();
                snapshot.setRecordId(IdGenerator.getId());
                snapshot.setStudentId(student.getId());
                snapshot.setYear(year);
                snapshot.setMouth(month);
                snapshot.setSumFinalScore(student.getSpecialtyScore());
                snapshot.setSettleState(1L); // 设置为已结算
                snapshot.setSettleTime(currentTime);
                snapshot.setType("SpecialtyAward"); // 设置类型为奖项
                snapshots.add(snapshot);

                // 重置学生的specialtyScore为0
                student.setSpecialtyScore(0.0);
            }

            // 保存所有快照记录
            try (Transaction transaction = MonthlyPerformanceSnapshot.find.db().beginTransaction()) {
                DB.saveAll(snapshots);
                transaction.commit();
            } catch (Exception e) {
                logger.error("保存月度快照记录出错", e);
                throw e;
            }

            // 更新所有学生的specialtyScore
            try (Transaction transaction = Student.find.db().beginTransaction()) {
                DB.updateAll(allStudents);
                transaction.commit();
            } catch (Exception e) {
                logger.error("重置学生specialtyScore出错", e);
                throw e;
            }

            logger.info("成功统计并保存 {} 个学生的月度奖项分数（上个月：{}-{}）", allStudents.size(), year, month);
        } catch (Exception e) {
            logger.error("统计月度奖项分数出错", e);
        }
    }




}
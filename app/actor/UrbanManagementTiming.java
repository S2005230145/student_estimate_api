package actor;

import akka.actor.ActorSystem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ebean.DB;
import io.ebean.Transaction;
import models.business.HabitRecord;
import models.business.SpecialtyAward;
import models.business.Student;
import models.mouth.MonthlyPerformanceSnapshot;
import play.Logger;
import play.cache.NamedCache;


import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Singleton
public class UrbanManagementTiming {

    private final Logger.ALogger logger = Logger.of(UrbanManagementTiming.class);

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    private final ActorSystem actorSystem;

    private final double student_base_habit_record_score=15.0;

    private final static ObjectMapper objectMapper=new ObjectMapper();

    @Inject
    @NamedCache("redis")
    protected play.cache.redis.AsyncCacheApi redis;



    @Inject
    public UrbanManagementTiming(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;

        // 调度任务
//        actorSystem.scheduler().scheduleOnce(
//                scala.concurrent.duration.Duration.create(0, TimeUnit.SECONDS),
//                this::scheduleFenceTask,
//                actorSystem.dispatcher()
//        );

        actorSystem.scheduler().scheduleOnce(
                scala.concurrent.duration.Duration.create(0, TimeUnit.SECONDS),
                this::scheduleMonth,
                actorSystem.dispatcher()
        );

    }

    private void scheduleFenceTask() {
        actorSystem.scheduler().scheduleWithFixedDelay(
                scala.concurrent.duration.Duration.create(calculateInitialDelay(5, 0), TimeUnit.SECONDS),
                scala.concurrent.duration.Duration.create(24 * 60 * 60, TimeUnit.SECONDS),
                () -> {
                    try {
                        List<Student> allStudent =null;
                        List<HabitRecord> allHabitRecord=null;
                        Optional<Object> obj1 = redis.sync().get("allStudent");
                        if(obj1.isPresent()){
                            allStudent =objectMapper.convertValue(obj1.get(),new TypeReference<>() {});
                        }else{
                            allStudent=Student.find.all();
                            redis.set("allStudent",Student.find.all());
                        }
                        Optional<Object> obj2 = redis.sync().get("allHabitRecord");
                        if(obj2.isPresent()){
                            allHabitRecord =objectMapper.convertValue(obj2.get(),new TypeReference<>() {});
                        }else{
                            allHabitRecord=HabitRecord.find.all();
                            redis.set("allHabitRecord",Student.find.all());
                        }
                        List<HabitRecord> habitRecordList = allHabitRecord.stream().filter(v1 -> v1.getMonthEndTime() != null).toList();
                        allStudent.forEach(student->{
                            double total = habitRecordList.stream()
                                    .filter(v1 -> v1.getStudentId() == student.getId())
                                    .map(HabitRecord::getScoreChange)
                                    .mapToDouble(Double::valueOf)
                                    .sum();
                            student.setHabitScore(student_base_habit_record_score+total);
                        });
                        try(Transaction transaction = Student.find.db().beginTransaction()){
                            DB.updateAll(allStudent);
                            transaction.commit();
                        } catch (Exception e) {
                            logger.error("每月更新学生分数出错", e);
                        }
                    } catch (Exception e) {
                        logger.error("月份定时出错", e);
                    }
                },
                actorSystem.dispatcher()
        );
    }

    //特长(每月计算一次)
    private void scheduleMonth(){
        actorSystem.scheduler().scheduleWithFixedDelay(
                scala.concurrent.duration.Duration.create(calculateInitialDelay(5, 0), TimeUnit.SECONDS),
                scala.concurrent.duration.Duration.create(24 * 60 * 60 * 30, TimeUnit.SECONDS),
                () -> {

                    try {
                        List<Student> allStudent = Student.find.all();
                        List<Long> studentIds = allStudent.stream().map(Student::getId).toList();

                        List<MonthlyPerformanceSnapshot> mpsList = MonthlyPerformanceSnapshot.find.query().where()
                                .in("student_id",studentIds)
                                .eq("settle_state", 0)
                                .findList();
                        allStudent.parallelStream().forEach(student -> {
                            //奖项
                            List<MonthlyPerformanceSnapshot> mpsListByStudent1 = mpsList.parallelStream()
                                    .filter(v1 -> v1.getStudentId() == student.getId()&&v1.getType().toLowerCase().contains("specialtyaward"))
                                    .toList();
                            //月总分
                            double sum1 = mpsListByStudent1.parallelStream()
                                    .map(MonthlyPerformanceSnapshot::getSumFinalScore)
                                    .filter(Objects::nonNull)
                                    .mapToDouble(Double::valueOf)
                                    .sum();
                            //月数
                            int size1 = mpsListByStudent1.size();
                            student.setSpecialtyScore(Math.round(sum1/size1));

                            //习惯
                            List<MonthlyPerformanceSnapshot> mpsListByStudent2 = mpsList.parallelStream()
                                    .filter(v1 -> v1.getStudentId() == student.getId()&&v1.getType().toLowerCase().contains("habit"))
                                    .toList();
                            //月总分
                            double sum2 = mpsListByStudent2.parallelStream()
                                    .map(MonthlyPerformanceSnapshot::getSumFinalScore)
                                    .filter(Objects::nonNull)
                                    .mapToDouble(Double::valueOf)
                                    .sum();
                            //月数
                            int size2 = mpsListByStudent2.size();
                            student.setHabitScore(Math.round(sum2/size2));
                        });
                        try(Transaction transaction = Student.find.db().beginTransaction()){
                            DB.updateAll(allStudent);
                            transaction.commit();
                        } catch (Exception e) {
                            logger.error("MonthlyPerformanceSnapshot更新学生分数出错", e);
                        }
                    } catch (Exception e) {
                        logger.error("MonthlyPerformanceSnapshot定时出错", e);
                    }
                },
                actorSystem.dispatcher()
        );
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


}
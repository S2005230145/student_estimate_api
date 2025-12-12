package actor;

import akka.actor.ActorSystem;
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



    @Inject
    @NamedCache("redis")
    protected play.cache.redis.AsyncCacheApi redis;



    @Inject
    public UrbanManagementTiming(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;

        // 调度任务
        actorSystem.scheduler().scheduleOnce(
                scala.concurrent.duration.Duration.create(0, TimeUnit.SECONDS),
                this::scheduleFenceTask,
                actorSystem.dispatcher()
        );

        actorSystem.scheduler().scheduleOnce(
                scala.concurrent.duration.Duration.create(0, TimeUnit.SECONDS),
                this::scheduleVehicleSituationTask,
                actorSystem.dispatcher()
        );

        actorSystem.scheduler().scheduleOnce(
                scala.concurrent.duration.Duration.create(0, TimeUnit.SECONDS),
                this::scheduleVehicleSituationTask2,
                actorSystem.dispatcher()
        );

        actorSystem.scheduler().scheduleOnce(
                scala.concurrent.duration.Duration.create(0, TimeUnit.SECONDS),
                this::scheduleVehicleSituationTask3,
                actorSystem.dispatcher()
        );

        actorSystem.scheduler().scheduleOnce(
                scala.concurrent.duration.Duration.create(0, TimeUnit.SECONDS),
                this::scheduleVehicleSituationTask4,
                actorSystem.dispatcher()
        );

//        actorSystem.scheduler().scheduleOnce(
//                scala.concurrent.duration.Duration.create(0, TimeUnit.SECONDS),
//                this::scheduleVehicleSituationTask5,
//                actorSystem.dispatcher()
//        );

        actorSystem.scheduler().scheduleOnce(
                scala.concurrent.duration.Duration.create(0, TimeUnit.SECONDS),
                this::scheduleVehicleSituationTask6,
                actorSystem.dispatcher()
        );

        actorSystem.scheduler().scheduleOnce(
                scala.concurrent.duration.Duration.create(0, TimeUnit.SECONDS),
                this::scheduleVehicleSituationTask7,
                actorSystem.dispatcher()
        );

    }

    private void scheduleFenceTask() {
        actorSystem.scheduler().scheduleWithFixedDelay(
                scala.concurrent.duration.Duration.create(calculateInitialDelay(5, 0), TimeUnit.SECONDS),
                scala.concurrent.duration.Duration.create(24 * 60 * 60, TimeUnit.SECONDS),
                () -> {
                    try {
                    } catch (Exception e) {
                        logger.error("电子围栏自动下发-定时任务执行异常", e);
                    }
                },
                actorSystem.dispatcher()
        );
    }

    private void scheduleVehicleSituationTask() {
        actorSystem.scheduler().scheduleWithFixedDelay(
                scala.concurrent.duration.Duration.create(calculateInitialDelay(0, 0), TimeUnit.SECONDS),
                scala.concurrent.duration.Duration.create(24 * 60 * 60, TimeUnit.SECONDS),
                () -> {
                    try {
                        logger.info("车场停车情况-定时任务执行");

                    } catch (Exception e) {
                        logger.error("车场停车情况-定时任务执行异常", e);
                    }
                },
                actorSystem.dispatcher()
        );
    }

    private void scheduleVehicleSituationTask2() {
        actorSystem.scheduler().scheduleWithFixedDelay(
                scala.concurrent.duration.Duration.create(calculateInitialDelay(3, 0), TimeUnit.SECONDS),
                scala.concurrent.duration.Duration.create(24 * 60 * 60, TimeUnit.SECONDS),
                () -> {
                    try {
                        logger.info("车场停车情况-定时任务执行");
                    } catch (Exception e) {
                        logger.error("车场停车情况-定时任务执行异常", e);
                    }
                },
                actorSystem.dispatcher()
        );
    }

    private void scheduleVehicleSituationTask3() {
        actorSystem.scheduler().scheduleWithFixedDelay(
                scala.concurrent.duration.Duration.create(calculateInitialDelay(2, 0), TimeUnit.SECONDS),
                scala.concurrent.duration.Duration.create(24 * 60 * 60, TimeUnit.SECONDS),
                () -> {
                    try {
                        logger.info("设置/更新车辆运输次数Redis-GPS信息-定时任务执行");
                        setVehicleGps();
                    } catch (Exception e) {
                        logger.error("设置/更新车辆运输次数Redis-GPS信息-定时任务执行异常", e);
                    }
                },
                actorSystem.dispatcher()
        );
    }

    private void scheduleVehicleSituationTask4() {
        actorSystem.scheduler().scheduleWithFixedDelay(
                scala.concurrent.duration.Duration.create(calculateInitialDelay(0, 15), TimeUnit.SECONDS),
                scala.concurrent.duration.Duration.create(24 * 60 * 60, TimeUnit.SECONDS),
                () -> {
                    try {
                        // 精确判断是否为每月1号
                        if (java.time.LocalDate.now().getDayOfMonth() == 1) {
                            logger.info("月初街道考评数据生成-定时任务执行");

                        }
                    } catch (Exception e) {
                        logger.error("月初街道考评数据生成-定时任务执行异常", e);
                    }
                },
                actorSystem.dispatcher()
        );
    }

    private void scheduleVehicleSituationTask7() {
        actorSystem.scheduler().scheduleWithFixedDelay(
                scala.concurrent.duration.Duration.create(calculateInitialDelay(0, 1), TimeUnit.SECONDS),
                scala.concurrent.duration.Duration.create(24 * 60 * 60, TimeUnit.SECONDS),
                () -> {
                    try {
                        // 精确判断是否为每月1号
                        if (java.time.LocalDate.now().getDayOfMonth() == 1) {
                            logger.info("月初街道考评数据状态修改-定时任务执行");

                        }
                    } catch (Exception e) {
                        logger.error("月初街道考评数据状态修改-定时任务执行异常", e);
                    }
                },
                actorSystem.dispatcher()
        );
    }

    private void scheduleVehicleSituationTask5() {
        actorSystem.scheduler().scheduleWithFixedDelay(
                scala.concurrent.duration.Duration.create(calculateInitialDelay(0, 1), TimeUnit.SECONDS),
                scala.concurrent.duration.Duration.create(10 * 60, TimeUnit.SECONDS),
                () -> {
                    try {
                        logger.info("检测车辆连续行驶-定时任务执行");

                    } catch (Exception e) {
                        logger.error("检测车辆连续行驶-定时任务执行异常", e);
                    }
                },
                actorSystem.dispatcher()
        );
    }

    private void scheduleVehicleSituationTask6() {
        actorSystem.scheduler().scheduleWithFixedDelay(
                scala.concurrent.duration.Duration.create(calculateInitialDelay(0, 0), TimeUnit.SECONDS),
                scala.concurrent.duration.Duration.create(60, TimeUnit.SECONDS),
                () -> {
                    try {
//                        logger.info("检测车辆在线状态-定时任务执行");


                    } catch (Exception e) {
                        logger.error("检测车辆在线状态-定时任务执行异常", e);
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

    /**
     * 设置车辆GPS信息
     */
    public void setVehicleGps() {



    }


}
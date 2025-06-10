package service;

import actor.ActorProtocol;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import play.Logger;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 定时脚本处理器
 */
@Singleton
public class FixedTimeExecutor {
    private final ActorSystem system;
    Logger.ALogger logger = Logger.of(FixedTimeExecutor.class);
    
    
    @Inject
    public FixedTimeExecutor(ActorSystem system) {
        this.system = system;
        Executor executor = Executors.newCachedThreadPool();
        CompletableFuture.runAsync(() -> schedule(), executor);
    }
    
    public void schedule() {
        logger.info("schedule");
        
    }
    
    
}

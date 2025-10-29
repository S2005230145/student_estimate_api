package actor;

import akka.actor.AbstractLoggingActor;
import utils.DateUtils;

import javax.inject.Inject;

/**
 * Created by win7 on 2016/7/14.
 */
public class TimerActor extends AbstractLoggingActor {

    @Inject
    DateUtils dateUtils;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .build();
    }
 

}

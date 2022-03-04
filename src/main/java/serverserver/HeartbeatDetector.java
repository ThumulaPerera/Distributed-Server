package serverserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.command.leadertofollower.HbStatusCheckL2FCommand;
import state.ServerAvailability;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class HeartbeatDetector {
    private static final int HEARTBEAT_CHECK_INTERVAL = 10000;
    private static final float HEARTBEAT_CHECK_FRACTION = 0.9f;
    private static final int HEARTBEAT_FUNC_CONFIRM_INTERVAL = 3000;

    private static Logger LOGGER = LoggerFactory.getLogger(HeartbeatDetector.class);
    private HashMap<String, Long> serverUpdateTimes = new HashMap<>();
    private HashMap<String, ServerAvailability> serverAvailabilityStatus = new HashMap<>();
    private HashMap<String, TimerTask> serverTimeCheckTasks = new HashMap<>();
    private Timer taskScheduler = new Timer();

    public void updateTime(String serverID, long time) {
        if (serverUpdateTimes.containsKey(serverID)) {
            serverUpdateTimes.replace(serverID, time);
            serverTimeCheckTasks.get(serverID).cancel();
            if(serverAvailabilityStatus.get(serverID)!= ServerAvailability.ACTIVE){
                serverAvailabilityStatus.replace(serverID,ServerAvailability.ACTIVE);
            }
            checkHbIntervalTask task = new checkHbIntervalTask(serverID);
            taskScheduler.schedule(task, HEARTBEAT_CHECK_INTERVAL);
            serverTimeCheckTasks.replace(serverID,task);
        } else {
            serverUpdateTimes.put(serverID, time);
            serverAvailabilityStatus.put(serverID, ServerAvailability.ACTIVE);
            checkHbIntervalTask task = new checkHbIntervalTask(serverID);
            taskScheduler.schedule(task, HEARTBEAT_CHECK_INTERVAL);
            serverTimeCheckTasks.put(serverID,task);
        }
        LOGGER.debug(serverID + " time updated to: " + time);
    }

    public void handleHbStatusReply(String serverID){
        switch (serverAvailabilityStatus.get(serverID)){
            case ACTIVE -> {/*ignore*/}
            case SUSPICIOUS, INACTIVE -> {
                markActive(serverID);
                LOGGER.debug("Server "+serverID+" marked : ACTIVE");
                serverTimeCheckTasks.get(serverID).cancel();
                checkHbIntervalTask task = new checkHbIntervalTask(serverID);
                taskScheduler.schedule(task, HEARTBEAT_CHECK_INTERVAL);
                serverTimeCheckTasks.replace(serverID,task);
            }
        }
    }

    public void markSuspicious(String serverID) {
        serverAvailabilityStatus.replace(serverID, ServerAvailability.SUSPICIOUS);
    }

    public void markInactive(String serverID) {
        serverAvailabilityStatus.replace(serverID, ServerAvailability.INACTIVE);
    }

    public void markActive(String serverID) {
        serverAvailabilityStatus.replace(serverID, ServerAvailability.ACTIVE);
    }

    private class checkHbIntervalTask extends TimerTask {
        String serverID;

        public checkHbIntervalTask(String id){
            super();
            this.serverID = id;
        }

        @Override
        public void run() {

            long dif = System.currentTimeMillis()-serverUpdateTimes.get(serverID);
            if(dif > HEARTBEAT_CHECK_FRACTION * HEARTBEAT_CHECK_INTERVAL){
                markSuspicious(serverID);
                LOGGER.debug("Server "+serverID+" marked : SUSPICIOUS");
                Sender.sendCommandToFollowerAndReceive(new HbStatusCheckL2FCommand(), serverID);
                serverTimeCheckTasks.get(serverID).cancel();
                checkFunctionalityTask task = new checkFunctionalityTask(serverID);
                serverTimeCheckTasks.replace(serverID,task);
                taskScheduler.schedule(task,HEARTBEAT_FUNC_CONFIRM_INTERVAL);
            }else{
                LOGGER.debug("Server "+serverID+" not marked");
            }
        }
    }

    private class checkFunctionalityTask extends TimerTask {
        String serverID;

        public checkFunctionalityTask(String id){
            super();
            this.serverID = id;
        }

        @Override
        public void run() {
            markInactive(serverID);
            LOGGER.debug("Server "+serverID+" marked : INACTIVE");
        }
    }

}

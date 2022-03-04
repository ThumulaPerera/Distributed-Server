package serverserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.ServerAvailability;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class HeartbeatDetector {
    private static final int HEARTBEAT_CHECK_INTERVAL = 10000;
    private static final int HEARTBEAT_FUNC_CONFIRM_INTERVAL = 3000;

    private static Logger LOGGER = LoggerFactory.getLogger(HeartbeatDetector.class);
    private HashMap<String, Long> serverUpdateTimes = new HashMap<>();
    private HashMap<String, ServerAvailability> serverAvailabilityStatus = new HashMap<>();
    private HashMap<String, checkHbIntervalTask> serverTimeCheckTasks = new HashMap<>();
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
            //make sure interval exceeded
            if(System.currentTimeMillis()-serverUpdateTimes.get(serverID) > HEARTBEAT_CHECK_INTERVAL){
                markSuspicious(serverID);
                LOGGER.debug("Server "+serverID+" marked : SUSPICIOUS");
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
            //make sure interval exceeded
            if(System.currentTimeMillis()-serverUpdateTimes.get(serverID) > HEARTBEAT_CHECK_INTERVAL){
                markSuspicious(serverID);
                LOGGER.debug("Server "+serverID+" marked : SUSPICIOUS");
            }
        }
    }

}

package serverserver;

import command.Command;
import command.ExecutableCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.command.leadertofollower.HbStatusCheckL2FCommand;
import state.ServerAvailability;
import state.StateManagerImpl;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class HeartbeatDetector {
    private static final int HEARTBEAT_CHECK_INTERVAL = 10000;
    private static final float HEARTBEAT_CHECK_FRACTION = 0.9f;
    private static final int HEARTBEAT_FUNC_CONFIRM_INTERVAL = 3000;

    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatDetector.class);
    private final StateManagerImpl STATE_MANAGER = StateManagerImpl.getInstance();
    private final HashMap<String, Long> serverUpdateTimes = new HashMap<>();
    private final HashMap<String, ServerAvailability> serverAvailabilityStatus = new HashMap<>();
    private final HashMap<String, TimerTask> serverTimeCheckTasks = new HashMap<>();
    private final Timer taskScheduler = new Timer();

    public void updateTime(String serverID, long time) {
        if (serverUpdateTimes.containsKey(serverID)) {
            serverUpdateTimes.put(serverID, time);
            if (serverAvailabilityStatus.get(serverID) != ServerAvailability.ACTIVE) {
                LOGGER.debug("Server " + serverID + " again marked : ACTIVE");
                markActive(serverID);
            }
        } else {
            serverUpdateTimes.put(serverID, time);
            markActive(serverID);
        }
        scheduleHbCheckTask(serverID);
    }

    public void handleHbStatusReply(String serverID) {
        switch (serverAvailabilityStatus.get(serverID)) {
            case ACTIVE -> {/*ignore*/}
            case SUSPICIOUS, INACTIVE -> {
                markActive(serverID);
                scheduleHbCheckTask(serverID);
            }
        }
    }

    public void markSuspicious(String serverID) {
        serverAvailabilityStatus.put(serverID, ServerAvailability.SUSPICIOUS);
    }

    public void markInactive(String serverID) {
        serverAvailabilityStatus.put(serverID, ServerAvailability.INACTIVE);
    }

    public void markActive(String serverID) {
        serverAvailabilityStatus.put(serverID, ServerAvailability.ACTIVE);
    }

    private void scheduleHbCheckTask(String serverID) {
        TimerTask current = serverTimeCheckTasks.get(serverID);
        checkHbIntervalTask task = new checkHbIntervalTask(serverID);
        if (current != null) {
            current.cancel();
        }
        serverTimeCheckTasks.put(serverID, task);
        taskScheduler.schedule(task, HEARTBEAT_CHECK_INTERVAL);
    }

    private void scheduleStatusCheckTask(String serverID) {
        TimerTask current = serverTimeCheckTasks.get(serverID);
        checkFunctionalityTask task = new checkFunctionalityTask(serverID);
        if (current != null) {
            current.cancel();
        }
        serverTimeCheckTasks.put(serverID, task);
        taskScheduler.schedule(task, HEARTBEAT_FUNC_CONFIRM_INTERVAL);
    }

    private class checkHbIntervalTask extends TimerTask {
        String serverID;

        public checkHbIntervalTask(String id) {
            super();
            this.serverID = id;
        }

        @Override
        public void run() {

            long dif = System.currentTimeMillis() - serverUpdateTimes.get(serverID);
            if (dif > HEARTBEAT_CHECK_FRACTION * HEARTBEAT_CHECK_INTERVAL) {
                markSuspicious(serverID);
                LOGGER.debug("Server " + serverID + " marked : SUSPICIOUS");
                ExecutableCommand statusReply = Sender.sendCommandToPeerAndReceive(
                        new HbStatusCheckL2FCommand(), STATE_MANAGER.getServer(serverID));
                //TODO is there a better way to handle
                if (statusReply != null) {
                    statusReply.execute();
                }
                scheduleStatusCheckTask(serverID);
            } else {
                LOGGER.debug("Server " + serverID + " not marked");
            }
        }
    }

    private class checkFunctionalityTask extends TimerTask {
        String serverID;

        public checkFunctionalityTask(String id) {
            super();
            this.serverID = id;
        }

        @Override
        public void run() {
            markInactive(serverID);
            LOGGER.debug("Server " + serverID + " marked : INACTIVE");
        }
    }

}

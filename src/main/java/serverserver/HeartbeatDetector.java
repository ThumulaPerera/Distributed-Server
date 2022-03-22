package serverserver;

import command.ExecutableCommand;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.command.leadertofollower.HbStatusCheckL2FCommand;
import serverserver.command.leadertofollower.HbStatusNotifyL2FCommand;
import state.RefinedStateManagerImpl;
import state.ServerAvailability;
import state.ServerModel;
import state.StateManager;

import java.util.*;

public class HeartbeatDetector {
    private static final int HEARTBEAT_CHECK_INTERVAL = 6000;
    private static final float HEARTBEAT_CHECK_FRACTION = 0.9f;
    private static final int HEARTBEAT_FUNC_CONFIRM_INTERVAL = 3000;

    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatDetector.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();
    private final Map<String, Long> serverUpdateTimes = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, ServerAvailability> serverAvailabilityStatus = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, TimerTask> serverTimeCheckTasks = Collections.synchronizedMap(new HashMap<>());
    @Getter
    private final Set<String> activeServers = new HashSet<>();
    private final Timer taskScheduler = new Timer();


    public void stopDetector() {
        synchronized (serverTimeCheckTasks) {
            for (TimerTask t : serverTimeCheckTasks.values()) {
                t.cancel();
            }
        }
    }

    public void updateTime(String serverID, long time) {
        if (serverUpdateTimes.containsKey(serverID)) {
            serverUpdateTimes.put(serverID, time);
            if (serverAvailabilityStatus.get(serverID) != ServerAvailability.ACTIVE) {
                if (serverAvailabilityStatus.get(serverID) == ServerAvailability.INACTIVE) {
//                    Sender.broadcastCommandToAllFollowers(new HbStatusNotifyL2FCommand(serverID, ServerAvailability.ACTIVE));
                }
                LOGGER.debug("Server " + serverID + " again marked : ACTIVE");
                markActive(serverID);
            }
        } else {
            serverUpdateTimes.put(serverID, time);
            markActive(serverID);
            //TODO: notify if needed initially
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
            //                Sender.broadcastCommandToAllFollowers(new HbStatusNotifyL2FCommand(serverID, ServerAvailability.ACTIVE));
        }
    }

    public void markSuspicious(String serverID) {
        serverAvailabilityStatus.put(serverID, ServerAvailability.SUSPICIOUS);
    }

    public void markInactive(String serverID) {
        serverAvailabilityStatus.put(serverID, ServerAvailability.INACTIVE);
        activeServers.remove(serverID);
    }

    public void markActive(String serverID) {
        serverAvailabilityStatus.put(serverID, ServerAvailability.ACTIVE);
        activeServers.add(serverID);
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
        private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();


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
                try{
                    ExecutableCommand statusReply = Sender.sendCommandToPeerAndReceive(
                            new HbStatusCheckL2FCommand(), STATE_MANAGER.getServer(serverID));
                    if (statusReply != null) {
                        statusReply.execute();
                    }
                }catch (Exception e){
                    LOGGER.error(e.getMessage());
                }

                scheduleStatusCheckTask(serverID);
            } else {
                LOGGER.debug("Server " + serverID + " not marked");
            }
        }
    }

    private class checkFunctionalityTask extends TimerTask {
        String serverID;
        private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();


        public checkFunctionalityTask(String id) {
            super();
            this.serverID = id;
        }

        @Override
        public void run() {
            markInactive(serverID);

            //update my (leader) state
            STATE_MANAGER.removeAvailableServerId(serverID);
            STATE_MANAGER.removeAllChatRoomsOfRemoteServer(serverID);
            STATE_MANAGER.removeClientsOfRemoteServer(serverID);

            //inform followers about the inactivity of this server
            Sender.broadcastCommandToAllFollowers(new HbStatusNotifyL2FCommand(serverID));
            LOGGER.debug("Server " + serverID + " marked : INACTIVE");
        }
    }

}

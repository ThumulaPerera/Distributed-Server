package serverserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.ServerAvailability;

import java.util.HashMap;

public class HeartbeatDetector{
    private static Logger LOGGER = LoggerFactory.getLogger(HeartbeatDetector.class);
    private HashMap<String, Long> serverUpdateTimes = new HashMap<>();
    private HashMap<String, ServerAvailability> serverAvailabilityStatus = new HashMap<>();

    public void updateTime(String serverID, long time){
        if(serverUpdateTimes.containsKey(serverID)){
            serverUpdateTimes.replace(serverID,time);
        }else{
            serverUpdateTimes.put(serverID,time);
            serverAvailabilityStatus.put(serverID,ServerAvailability.ACTIVE);
        }
        LOGGER.debug(serverID + " time updated to: "+ time);
    }

    public void markSuspicious(String serverID){
        serverAvailabilityStatus.replace(serverID,ServerAvailability.SUSPICIOUS);
    }

    public void markInactive(String serverID){
        serverAvailabilityStatus.replace(serverID,ServerAvailability.INACTIVE);
    }

    public void markActive(String serverID){
        serverAvailabilityStatus.replace(serverID,ServerAvailability.ACTIVE);
    }


}

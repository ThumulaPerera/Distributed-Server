package serverserver;

import command.ExecutableCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.command.followertoleader.HeartbeatF2LCommand;
import state.RefinedStateManagerImpl;
import state.StateManager;

import java.util.Timer;
import java.util.TimerTask;

public class HeartbeatPulser {
    private static final int HEARTBEAT_PULSE_INTERVAL = 3000;
    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatPulser.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    public void initiatePulse() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!STATE_MANAGER.isLeader()) {
                    sendPulse();
                }
            }
        }, 0, HEARTBEAT_PULSE_INTERVAL);
    }

    private void sendPulse() {
        try{
            ExecutableCommand serverListCommand = Sender.sendCommandToLeaderAndReceive(new HeartbeatF2LCommand(STATE_MANAGER.getSelf().getId()));
            if (serverListCommand != null) {
                serverListCommand.execute();
            }
        } catch (Exception ignored){

        }

    }
}

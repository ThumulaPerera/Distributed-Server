package serverserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.command.followertoleader.HeartbeatF2LCommand;
import state.StateManager;
import state.StateManagerImpl;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This thread is responsible to handle client connection. * * @author www.codejava.net
 */
public class HeartbeatPulser {
    private static final int HEARTBEAT_PULSE_INTERVAL = 5000;
    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatPulser.class);
    private static final StateManager STATE_MANAGER = StateManagerImpl.getInstance();

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
        Sender.sendCommandToLeader(new HeartbeatF2LCommand(STATE_MANAGER.getSelf().getId()));
    }
}

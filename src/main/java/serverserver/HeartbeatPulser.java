package serverserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.command.followertoleader.HeartbeatF2LCommand;
import state.StateManager;
import state.StateManagerImpl;
import utils.JsonParser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This thread is responsible to handle client connection. * * @author www.codejava.net
 */
public class HeartbeatPulser {
    private static final int HEARTBEAT_PULSE_INTERVAL = 5000;
    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatPulser.class);
    private static final StateManager STATE_MANAGER = StateManagerImpl.getInstance();

    public HeartbeatPulser() {
    }

    public void initiatePulse() {
        Sender sender = new Sender();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!STATE_MANAGER.isLeader()) {
                    sendPulse(sender);
                }
            }
        }, 0, HEARTBEAT_PULSE_INTERVAL);
    }

    private void sendPulse(Sender sender) {
        sender.sendCommandToLeader(new HeartbeatF2LCommand(STATE_MANAGER.getSelf().getId()));
    }
}

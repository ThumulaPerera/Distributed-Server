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
    private static Logger LOGGER = LoggerFactory.getLogger(HeartbeatPulser.class);
    private static final StateManager STATE_MANAGER = StateManagerImpl.getInstance();

    private String address;
    private int port;

    public HeartbeatPulser(String address, int port) {
        this.address = address;
        this.port = port;
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
//                try {
//                    if (!Objects.equals(Config.getServerId(), Config.getLeaderServerId())) {
//                        Socket socket = new Socket(address, port);
//                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//                        sendPulse(bw);
//                        socket.close();
//                    }
//                } catch (IOException ex) {
//                    System.out.println("serverserver.Server exception: " + ex.getMessage());
//                    ex.printStackTrace();
//                }
            }
        }, 0, 5000);
//            sendPulse(bw);
//            Thread.sleep(10000);
//            sendPulse(bw);
//            Thread.sleep(3100);
//
//            sendPulse(bw);


    }

//    private void sendPulse(BufferedWriter bw) {
//        try {
//            if (!Objects.equals(Config.getServerId(), Config.getLeaderServerId())) {
//                LOGGER.debug("Sent HB message from " + Config.getServerId());
//                bw.write("{\"type\" : \"heartbeat\", \"from\" : \"" + Config.getServerId() + "\"}");
//                bw.newLine();
//                bw.flush();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void sendPulse(Sender sender) {
        sender.sendCommandToLeader(new HeartbeatF2LCommand(STATE_MANAGER.getSelf().getId()));
    }
}

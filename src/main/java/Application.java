import clientserver.Server;
import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.FastBully;
import serverserver.HeartbeatPulser;
import serverserver.Receiver;
import state.RefinedStateManagerImpl;
import state.StateManager;
import utils.CliArgParser;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;


public class Application {

    public static void main(String[] args) throws InterruptedException {
        initConfig(args);
        Logger LOGGER = LoggerFactory.getLogger(Application.class);
        StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

        Thread serverToServerReceiver = new Thread(new Receiver());
        serverToServerReceiver.start();

        FastBully.executeStartup();
        STATE_MANAGER.setElectionAllowed(false);
        LOGGER.debug("servers: " + STATE_MANAGER.getAvailableServerIds().toString() +
                "\tLeader: " + STATE_MANAGER.getLeader().getId());


        new HeartbeatPulser().initiatePulse();


        Thread serverThread = new Thread(new Server());
        serverThread.start();
//        if (STATE_MANAGER.getSelf().getId().equals("s1")) {
//            new Thread(() -> {
//                try {
//                    Thread.sleep(20000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                FastBully.startElection();
//            }).start();
//        }

        while (true) {
            Thread.sleep(2000);
            LOGGER.debug("Leader: " + STATE_MANAGER.getLeader().getId()+ "\t servers: "+ STATE_MANAGER.getAvailableServerIds());
        }
    }

    public static void initConfig(String[] args) {
        Map<String, String> parsedArgs = CliArgParser.parse(args);

        String serverId = parsedArgs.get("serverid");
        Path serverConfigPath = Paths.get(parsedArgs.get("servers_conf"));

        Config.loadProperties(serverId, serverConfigPath);
    }
}

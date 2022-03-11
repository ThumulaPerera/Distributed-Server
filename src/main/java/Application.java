import clientserver.Server;
import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.FastBully;
import serverserver.HeartbeatPulser;
import serverserver.Receiver;
import serverserver.Sender;
import serverserver.command.fastbully.CoordinatorCommand;
import serverserver.command.fastbully.IamUpCommand;
import serverserver.command.fastbully.ViewCommand;
import state.StateManagerImpl;
import utils.CliArgParser;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class Application {

    public static void main(String[] args) throws InterruptedException {
        initConfig(args);
        Logger LOGGER = LoggerFactory.getLogger(Application.class);
        StateManagerImpl STATE_MANAGER = StateManagerImpl.getInstance();

        Thread serverToServerReceiver = new Thread(new Receiver());
        serverToServerReceiver.start();

        FastBully.executeStartup();
        STATE_MANAGER.setElectionAllowed(false);
        LOGGER.debug("servers: " + STATE_MANAGER.getAvailableServers().toString() +
                "\tLeader: " + STATE_MANAGER.getLeader().getId());


        new HeartbeatPulser().initiatePulse();

//        if (STATE_MANAGER.getSelf().getId().equals("s1")){
//            new Thread(()->{
//                try {
//                    Thread.sleep(20000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                FastBully.startElection();}).start();
//        }



        Thread serverThread = new Thread(new Server());
        serverThread.start();
        while (true) {
//            LOGGER.debug("Leader: " + STATE_MANAGER.getLeader().getId());
            Thread.sleep(2000);
            LOGGER.debug("Leader: " + STATE_MANAGER.getLeader().getId());
            Thread.sleep(20000);
        }
    }

    public static void initConfig(String[] args) {
        Map<String, String> parsedArgs = CliArgParser.parse(args);

        String serverId = parsedArgs.get("serverid");
        Path serverConfigPath = Paths.get(parsedArgs.get("servers_conf"));

        Config.loadProperties(serverId, serverConfigPath);
    }
}

import clientserver.Server;
import config.Config;
import lombok.extern.slf4j.Slf4j;
import serverserver.FastBully;
import serverserver.HeartbeatPulser;
import serverserver.Receiver;
import state.RefinedStateManagerImpl;
import state.StateManager;
import utils.CliArgParser;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;


@Slf4j
public class Application {

    public static void main(String[] args) throws InterruptedException {
        initConfig(args);

        StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

        Thread serverToServerReceiver = new Thread(new Receiver());
        serverToServerReceiver.start();

        FastBully.executeStartup();
        STATE_MANAGER.setElectionAllowed(false);
        log.debug("servers: " + STATE_MANAGER.getAvailableServerIds().toString() +
                "\tLeader: " + STATE_MANAGER.getLeader().getId());


        new HeartbeatPulser().initiatePulse();


        Thread serverThread = new Thread(new Server());
        serverThread.start();

        while (true) {
            Thread.sleep(2000);
            log.debug("Leader: " + STATE_MANAGER.getLeader().getId()+ "\t servers: "+ STATE_MANAGER.getAvailableServerIds());
        }
    }

    public static void initConfig(String[] args) {
        Map<String, String> parsedArgs = CliArgParser.parse(args);

        log.debug("parsed args: " + parsedArgs.toString());

        String serverId = parsedArgs.get("serverid");
        Path serverConfigPath = Paths.get(parsedArgs.get("servers_conf"));

        Config.loadProperties(serverId, serverConfigPath);

        if (parsedArgs.containsKey("socket_timeout")) {
            Config.setSOCKET_TIMEOUT(Integer.parseInt(parsedArgs.get("socket_timeout")));
        }
    }
}

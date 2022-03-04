import clientserver.Server;
import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

//         sending Iamup messages on start
        // TODO: setup T2 timeout for this
        STATE_MANAGER.getServers().forEach((serverId, server) -> {
            ViewCommand viewCommand = (ViewCommand) Sender.sendCommandToPeerAndReceive(
                    new IamUpCommand(), server);

            if (viewCommand != null) {
                viewCommand.execute();
            }
        });
        STATE_MANAGER.addAvailableServer(STATE_MANAGER.getSelf().getId());
        Set<String> availableServers = STATE_MANAGER.getAvailableServers();
        synchronized (availableServers){
            String possibleLeader = Collections.max(STATE_MANAGER.getAvailableServers());
            if (possibleLeader.equals(STATE_MANAGER.getSelf().getId())){
                STATE_MANAGER.setLeader(possibleLeader);
                // sending coordinator message to lower ranked processes
                CoordinatorCommand command = new CoordinatorCommand();
                command.setFrom(possibleLeader);
                for(String serverId: availableServers){
                    if (serverId.compareTo(possibleLeader) < 0){
                        Sender.sendCommandToPeer(command, STATE_MANAGER.getServers().get(serverId));
                    }
                }
            }
        }


        STATE_MANAGER.setLeader(Collections.max(STATE_MANAGER.getAvailableServers()));
        LOGGER.debug("servers: " + STATE_MANAGER.getAvailableServers().toString() + "\tLeader: " + STATE_MANAGER.getLeader().getId());


//        new HeartbeatPulser().initiatePulse();

        Thread serverThread = new Thread(new Server());
        serverThread.start();
        while(true){
            LOGGER.debug("Leader: "+ STATE_MANAGER.getLeader().getId());
            Thread.sleep(3000);
        }
    }

    public static void initConfig(String[] args) {
        Map<String, String> parsedArgs = CliArgParser.parse(args);

        String serverId = parsedArgs.get("serverid");
        Path serverConfigPath = Paths.get(parsedArgs.get("servers_conf"));

        Config.loadProperties(serverId, serverConfigPath);
    }
}

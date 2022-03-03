import clientserver.Server;
import config.Config;
import serverserver.HeartbeatPulser;
import serverserver.Receiver;
import utils.CliArgParser;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Application {

    public static void main(String[] args) {
        initConfig(args);

        Thread serverToServerReceiver = new Thread(new Receiver());
        serverToServerReceiver.start();

        String leader = Config.getLeaderServerId();
        int port = Config.getCoordinationPort(leader);
        String address = Config.getServerAddress(leader);

        new HeartbeatPulser(address, port).initiatePulse();

        Thread serverThread = new Thread(new Server());
        serverThread.start();
    }

    public static void initConfig(String[] args) {
        Map<String, String> parsedArgs = CliArgParser.parse(args);

        String serverId = parsedArgs.get("serverid");
        Path serverConfigPath = Paths.get(parsedArgs.get("servers_conf"));

        Config.loadProperties(serverId, serverConfigPath);
    }
}

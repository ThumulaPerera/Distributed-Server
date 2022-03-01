import clientserver.Server;
import config.Config;
import utils.CliArgParser;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Application {

    public static void main(String[] args) {
        initConfig(args);

        Thread serverThread = new Thread(new Server());
        serverThread.start();
    }

    public static void initConfig(String[] args) {
        Map<String,String> parsedArgs = CliArgParser.parse(args);

        String serverId = parsedArgs.get("serverid");
        Path serverConfigPath = Paths.get(parsedArgs.get("servers_conf"));

        Config.loadProperties(serverId, serverConfigPath);
    }
}

package config;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.StateInitializer;
import state.StateManager;
import state.StateManagerImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Properties;

public class Config {
    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);
    private static final StateManager STATE_MANAGER = StateManagerImpl.getInstance();
    private static Properties PROPERTIES;

    @Getter private static final int FASTBULLY_T1 = 5000;
    @Getter private static final int FASTBULLY_T2 = 5000;
    @Getter private static final int FASTBULLY_T3 = 5000;

    public static void loadProperties(String serverID, Path path) {
//        PROPERTIES = new Properties();
        StateInitializer stateInitializer = StateManagerImpl.getInstance();


        try {

//            Files.lines(path)
//                    .filter(line -> line.split("\t", 4)[0].equals(serverID))
//                    .findFirst()
//                    .map(line -> {
//                        String[] arrOfStr = line.split("\t", 4);
//                        PROPERTIES.setProperty("serverid", arrOfStr[0]);
//                        PROPERTIES.setProperty("server_address", arrOfStr[1]);
//                        PROPERTIES.setProperty("clients_port", arrOfStr[2]);
//                        PROPERTIES.setProperty("coordination_port", arrOfStr[3]);
//                        printDebugConfig();
//                        return null;
//                    });
            Files.lines(path)
                    .forEach(line -> {
                        LOGGER.debug("read config line: " + line);
                        String[] arrOfStr = line.split("\t", 4);
                       LOGGER.debug(Arrays.toString(arrOfStr));
                        stateInitializer.addServer(
                                arrOfStr[0],
                                arrOfStr[1],
                                Integer.parseInt(arrOfStr[2]),
                                Integer.parseInt(arrOfStr[3])
                        );
                    });
            STATE_MANAGER.setSelf(serverID);

            // TODO : temporary solution. Need to be changed.
            STATE_MANAGER.setLeader("s1");

        } catch (ArrayIndexOutOfBoundsException e) {
            LOGGER.error("Config file is not in correct format");
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static String getServerId() {
//        return PROPERTIES.getProperty("serverid");
        return STATE_MANAGER.getSelf().getId();
    }

    public static String getServerAddress() {
//        return PROPERTIES.getProperty("server_address");
        return STATE_MANAGER.getSelf().getAddress();
    }

    public static String getServerAddress(String id) {
        //TODO implement properly
        return "localhost";
    }


    public static int getClientsPort() {
//        return Integer.parseInt(PROPERTIES.getProperty("clients_port"));
        return STATE_MANAGER.getSelf().getClientsPort();
    }

    public static int getCoordinationPort() {
//        return Integer.parseInt(PROPERTIES.getProperty("coordination_port"));
        return STATE_MANAGER.getSelf().getCoordinationPort();
    }

    public static int getCoordinationPort(String id) {
        //TODO implement properly
        return switch (id) {
            case "s1" -> 5555;
            case "s2" -> 5556;
            case "s3" -> 5557;
            default -> 0;
        };
    }

    public static String getLeaderServerId(){
        //TODO implement properly
        return "s1";
    }


    private static void printDebugConfig() {
        LOGGER.debug("==== Config ====");
        LOGGER.debug("clientserver.Server ID         : " + getServerId());
        LOGGER.debug("clientserver.Server Address    : " + getServerAddress());
        LOGGER.debug("Clients Port      : " + getClientsPort());
        LOGGER.debug("Coordination Port : " + getCoordinationPort());
        LOGGER.debug("=================");
    }

}




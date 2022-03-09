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

    @Getter private static final int FASTBULLY_T1 = 5000;
    @Getter private static final int FASTBULLY_T2 = 5000;
    @Getter private static final int FASTBULLY_T3 = 5000;

    public static void loadProperties(String serverID, Path path) {
        StateInitializer stateInitializer = StateManagerImpl.getInstance();


        try {
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
        return STATE_MANAGER.getSelf().getId();
    }

    public static String getServerAddress() {
        return STATE_MANAGER.getSelf().getAddress();
    }

    public static int getClientsPort() {
        return STATE_MANAGER.getSelf().getClientsPort();
    }

    public static int getCoordinationPort() {
        return STATE_MANAGER.getSelf().getCoordinationPort();
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




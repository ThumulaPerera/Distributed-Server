package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Config {
    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);
    private static Properties PROPERTIES;

    public static void loadProperties(String serverID, Path path) {
        PROPERTIES = new Properties();

        try {

            Files.lines(path)
                    .filter(line -> line.split("\t", 4)[0].equals(serverID))
                    .findFirst()
                    .map(line -> {
                        String[] arrOfStr = line.split("\t", 4);
                        PROPERTIES.setProperty("serverid", arrOfStr[0]);
                        PROPERTIES.setProperty("server_address", arrOfStr[1]);
                        PROPERTIES.setProperty("clients_port", arrOfStr[2]);
                        PROPERTIES.setProperty("coordination_port", arrOfStr[3]);
                        printDebugConfig();
                        return null;
                    });

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static String getServerId() {
        return PROPERTIES.getProperty("serverid");
    }

    public static String getServerAddress() {
        return PROPERTIES.getProperty("server_address");
    }

    public static int getClientsPort() {
        return Integer.parseInt(PROPERTIES.getProperty("clients_port"));
    }

    public static int getCoordinationPort() {
        return Integer.parseInt(PROPERTIES.getProperty("coordination_port"));
    }

    private static void printDebugConfig() {
        LOGGER.debug("==== Config ====");
        LOGGER.debug("Server ID         : " + getServerId());
        LOGGER.debug("Server Address    : " + getServerAddress());
        LOGGER.debug("Clients Port      : " + getClientsPort());
        LOGGER.debug("Coordination Port : " + getCoordinationPort());
        LOGGER.debug("=================");
    }

}




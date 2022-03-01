package clientserver;

import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;


public class Server implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public void run() {
        int port = Config.getClientsPort();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            LOGGER.info("clientserver.Server is listening on port {}", port);
            while (true) {
                Socket socket = serverSocket.accept();
                LOGGER.info("New client connected : {}", socket);
                new ServerThread(socket).start();
            }
        } catch (IOException e) {
            LOGGER.error("clientserver.Server exception: {}", e.getMessage());
            e.printStackTrace();
        }
    }

}
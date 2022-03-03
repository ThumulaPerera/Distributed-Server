package serverserver;

import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

    public Receiver() {
    }

    @Override
    public void run() {
        int port = Config.getCoordinationPort();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            LOGGER.info("serverserver.Server is listening on port {}", port);
            while (true) {
                Socket socket = serverSocket.accept();
                LOGGER.info("New Server connected : {}", socket);
                new ReceiverThread(socket).start();
            }
        } catch (IOException e) {
            LOGGER.error("serverserver.Server exception: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}

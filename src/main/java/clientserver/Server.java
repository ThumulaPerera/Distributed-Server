package clientserver;

import com.google.common.util.concurrent.MoreExecutors;
import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;


public class Server implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            Config.getMIN_CLIENT_THREADS(),
            Config.getMAX_CLIENT_THREADS(),
            0,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>());
    private final ExecutorService executorService = MoreExecutors.getExitingExecutorService(executor, 1, TimeUnit.SECONDS);

    public void run() {
        int port = Config.getClientsPort();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            LOGGER.info("clientserver.Server is listening on port {}", port);
            while (true) {
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(Config.getSOCKET_TIMEOUT());
                LOGGER.info("New client connected on : {}", socket.getRemoteSocketAddress());
                executorService.submit(new ServerThread(socket));
            }
        } catch (IOException e) {
            LOGGER.error("clientserver.Server exception: {}", e.getMessage());
            e.printStackTrace();
        }
    }

}
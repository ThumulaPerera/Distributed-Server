package serverserver;

import com.google.common.util.concurrent.MoreExecutors;
import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Receiver implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            Config.getMIN_SERVER_THREADS(),
            Config.getMAX_SERVER_THREADS(),
            0,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>());
    private final ExecutorService executorService = MoreExecutors.getExitingExecutorService(executor, 1, TimeUnit.SECONDS);

    public Receiver() {
    }

    @Override
    public void run() {
        int port = Config.getCoordinationPort();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            LOGGER.info("serverserver.Receiver is listening on port {}", port);
            while (true) {
                Socket socket = serverSocket.accept();
                LOGGER.debug("New peer connected : {}", socket.getRemoteSocketAddress());
                executorService.submit(new ReceiverThread(socket));
            }
        } catch (IOException e) {
            LOGGER.error("clientserver.Server exception: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}

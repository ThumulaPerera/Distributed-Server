//package servertoserver;
//
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//public class Receiver implements Runnable {
//    private static final int PORT;
//
//    public Receiver() {
//    }
//
//    @Override
//    public void run() {
//        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
//            LOGGER.info("Server is listening on port {}", PORT);
//            while (true) {
//                Socket socket = serverSocket.accept();
//                LOGGER.info("New client connected : {}", socket);
//                new ServerThread(socket).start();
//            }
//        } catch (IOException e) {
//            LOGGER.error("Server exception: {}", e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}

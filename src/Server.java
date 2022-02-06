import java.net.*;
import java.io.*;

public class Server {
    private Socket socket;
    private ServerSocket server;

    public Server(int port){
        try {
            server = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            socket = server.accept();
            System.out.println("Client connected");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String line;
                while ( (line = br.readLine()) != null) {
                    System.out.println(line);
                }
            }

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server(5000);
    }

}

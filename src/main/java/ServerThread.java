import utils.JsonParser;

import java.io.*;
import java.net.*;

/**
 * This thread is responsible to handle client connection. * * @author www.codejava.net
 */
public class ServerThread extends Thread {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            try (
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)
            ) {
                String json;
                while ((json = br.readLine()) != null) {
                    System.out.println(json);

                    System.out.println(JsonParser.parse(json).get("type"));

                    pw.println("{\"type\" : \"newidentity\", \"approved\" : \"true\"}");
                }
            } catch (IOException ex) {
                System.out.println("Server exception: " + ex.getMessage());
                ex.printStackTrace();
            }
            socket.close();
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

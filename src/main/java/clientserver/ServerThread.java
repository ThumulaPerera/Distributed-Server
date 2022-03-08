package clientserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import command.Command;
import command.ExecutableCommand;
import clientserver.command.clienttoserver.C2SCommandFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.JsonParser;

import java.io.*;
import java.net.*;

/**
 * This thread is responsible to handle client connection. * * @author www.codejava.net
 */
public class ServerThread extends Thread {
    private static Logger LOGGER = LoggerFactory.getLogger(ServerThread.class);
    private static final ObjectMapper MAPPER = JsonParser.getMapper();

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
                    LOGGER.debug("Received from client: " + json);

                    ExecutableCommand command = C2SCommandFactory.createC2SCommand(json);
                    Command outputMessage = command.execute();

                    if (outputMessage != null) {
                        String jsonOutputMessage = MAPPER.writeValueAsString(outputMessage);
                        LOGGER.debug("Sending to client: " + jsonOutputMessage);
                        pw.println(jsonOutputMessage);
                    }
                }
            } catch (IOException ex) {
                LOGGER.error("clientserver.Server exception: " + ex.getMessage());
                ex.printStackTrace();
            }
            LOGGER.debug("Closing connection with client : {}", socket.getRemoteSocketAddress());
            socket.close();
        } catch (IOException ex) {
            LOGGER.error("clientserver.Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

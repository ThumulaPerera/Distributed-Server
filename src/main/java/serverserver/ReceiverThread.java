package serverserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import command.Command;
import command.ExecutableCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.command.S2SCommandFactory;
import utils.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This thread is responsible to handle client connection. * * @author www.codejava.net
 */
public class ReceiverThread extends Thread {
    private static Logger LOGGER = LoggerFactory.getLogger(ReceiverThread.class);
    private static final ObjectMapper MAPPER = JsonParser.getMapper();

    private Socket socket;

    public ReceiverThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            try (
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)
            ) {
                String json;
                json = br.readLine();

                LOGGER.debug("Received from peer: " + json);

                ExecutableCommand command = S2SCommandFactory.createCommand(json);
                Command outputMessage = command.execute();

                if (outputMessage != null) {
                    String outputMessageJson = MAPPER.writeValueAsString(outputMessage);
                    LOGGER.debug("Sending to peer: " + outputMessageJson);
                    pw.println(outputMessageJson);
                }

            } catch (IOException ex) {
                LOGGER.error("serverserver.Server exception: " + ex.getMessage());
                ex.printStackTrace();
            }
            LOGGER.debug("Closing connection: " + socket.getRemoteSocketAddress());
            socket.close();
        } catch (IOException ex) {
            LOGGER.error("serverserver.Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

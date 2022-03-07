package serverserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.command.S2SCommandFactory;
import state.ServerModel;
import state.StateManager;
import state.StateManagerImpl;
import utils.JsonParser;

import java.io.*;
import java.net.Socket;

public class Sender {
    private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);
    private static final ObjectMapper MAPPER = JsonParser.getMapper();
    private static final StateManager STATE_MANAGER = StateManagerImpl.getInstance();

    public Command sendCommandToLeader(Command command) {
        Command response = null;

        if (command == null) {
            LOGGER.error("Command is null");
            return null;
        }

        ServerModel leader = STATE_MANAGER.getLeader();

        try (Socket socket = new Socket(leader.getAddress(), leader.getCoordinationPort());
             BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String sentMessage = MAPPER.writeValueAsString(command);
            pw.println(sentMessage);
            LOGGER.debug("Sending message to leader: " + sentMessage);

            String receivedMessage;
            receivedMessage = br.readLine();
            LOGGER.debug("Received message from leader: " + receivedMessage);

            response = S2SCommandFactory.createCommand(receivedMessage);

            LOGGER.debug("Closing connection to leader: {}", socket.getRemoteSocketAddress());
        }catch(IOException e) {
            LOGGER.error("Error while sending command to leader: {}", e.getMessage());
            e.printStackTrace();
        }

        return response;
    }
}

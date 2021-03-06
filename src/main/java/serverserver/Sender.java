package serverserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import command.Command;
import command.ExecutableCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.command.S2SCommandFactory;
import state.RefinedStateManagerImpl;
import state.ServerModel;
import state.StateManager;
import utils.JsonParser;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Sender {
    private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);
    private static final ObjectMapper MAPPER = JsonParser.getMapper();
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    public static ExecutableCommand sendCommandToLeaderAndReceive(Command command) throws IOException {
        return sendCommandToPeerAndReceive(command, STATE_MANAGER.getLeader());
    }

    public static ExecutableCommand sendCommandToPeerAndReceive(Command command, ServerModel peer) throws IOException {
        ExecutableCommand response = null;

        if (command == null) {
            LOGGER.error("Command is null");
            return null;
        }

//        ServerModel leader = STATE_MANAGER.getLeader();

        try (Socket socket = new Socket(peer.getAddress(), peer.getCoordinationPort());
             BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)
        ) {
            String sentMessage = MAPPER.writeValueAsString(command);
            pw.println(sentMessage);
            LOGGER.debug("Sending message to peer: " + sentMessage);

            String receivedMessage;
            receivedMessage = br.readLine();
            LOGGER.debug("Received message from peer: " + receivedMessage);

            response = S2SCommandFactory.createCommand(receivedMessage);

            LOGGER.debug("Closing connection to peer: {}", socket.getRemoteSocketAddress());
        } catch (IOException e) {
            LOGGER.error("Error while sending command to peer: {}", e.getMessage());
            throw e;
        }

        return response;
    }

    public static boolean sendCommandToLeader(Command command) throws IOException {
        return sendCommandToPeer(command, STATE_MANAGER.getLeader());
    }


    public static boolean sendCommandToPeer(Command command, ServerModel peer) throws IOException {

        if (command == null) {
            LOGGER.error("Command is null");
            return false;
        }

//        ServerModel leader = STATE_MANAGER.getLeader();

        try (Socket socket = new Socket(peer.getAddress(), peer.getCoordinationPort());
             BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)
        ) {
            String sentMessage = MAPPER.writeValueAsString(command);
            pw.println(sentMessage);
            LOGGER.debug("Sending message to peer server: {}, message: {}", peer.getId(), sentMessage);

            LOGGER.debug("Closing connection to peer: {}", socket.getRemoteSocketAddress());
        } catch (IOException e) {
            System.out.println(e);
            throw e;
        }
        return true;
    }

    // to be used by leader only
    public static boolean broadcastCommandToAllFollowers(Command command) {
        for (ServerModel follower: STATE_MANAGER.getAllRemoteServers()) {
            try{
                sendCommandToPeer(command, follower);
            }catch (Exception ignore){

            }
        }
        return true;
    }

    // to be used by leader only
    public static boolean broadcastCommandToOtherFollowers(Command command, String excludeServerId) {
        for (ServerModel follower: STATE_MANAGER.getAllRemoteServers()) {
            if (!follower.getId().equals(excludeServerId)) {
                try{
                    sendCommandToPeer(command, follower);
                }catch (Exception ignore){

                }
            }
        }
        return true;
    }


}

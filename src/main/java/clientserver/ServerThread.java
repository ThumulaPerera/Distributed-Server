package clientserver;

import clientserver.command.clienttoserver.MoveJoinC2SCommand;
import clientserver.command.clienttoserver.NewIdentityC2SCommand;
import clientserver.command.clienttoserver.QuitC2SCommand;
import clientserver.command.servertoclient.NewIdentityS2CCommand;
import clientserver.command.servertoclient.RoomChangeS2CCommand;
import com.fasterxml.jackson.core.JsonProcessingException;
import command.ClientKnownExecutableCommand;
import command.Command;
import command.ExecutableCommand;
import clientserver.command.clienttoserver.C2SCommandFactory;
import command.ClientAndSenderKnownExecutableCommand;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.LocalClientModel;
import state.RefinedStateManagerImpl;
import state.StateManager;

import java.io.*;
import java.net.*;

/**
 * This thread is responsible to handle client connection. * * @author www.codejava.net
 */
public class ServerThread implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerThread.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    @Getter
    private final Socket socket;
    private final ClientSender sender;
    private LocalClientModel client;

    public ServerThread(Socket socket) {
        this.socket = socket;
        sender = new ClientSender(socket);
    }

    public void run() {
        String json;
        ExecutableCommand inputCommand;
        Command outputCommand;

        try {
            try (
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ) {

                json = br.readLine();
                inputCommand = getCommand(json);

                if (inputCommand instanceof NewIdentityC2SCommand) {
                    boolean isApproved = handleNewIdentity(inputCommand);
                    if (!isApproved) {
                        closeSocket();
                        return;
                    }

                } else if (inputCommand instanceof MoveJoinC2SCommand) {
                    handleMoveJoin(inputCommand);

                } else {
                    LOGGER.error("Unknown initial command: {}", inputCommand);
                    closeSocket();
                    return;
                }

                while ((json = br.readLine()) != null) {
                    inputCommand = getCommand(json);
                    if (inputCommand instanceof NewIdentityC2SCommand || inputCommand instanceof MoveJoinC2SCommand) {
                        LOGGER.error("Un-allowed command for already connected client: {}", inputCommand);
                    } else if (inputCommand instanceof QuitC2SCommand) {
                        inputCommand.execute();
                        closeSocket();
                        return;
                    } else {
                        outputCommand = inputCommand.execute();
                        sendResponse(outputCommand);
                    }
                }
            } catch (IOException ex) {
                LOGGER.error("Error while reading from client [" + client + "]", ex);
                handleIoException(ex);
            }
            LOGGER.info("Client [{}] abruptly disconnected: {}:{} ", client, socket.getInetAddress(), socket.getPort());

            QuitC2SCommand abruptQuitCommand = new QuitC2SCommand();
            setClient(abruptQuitCommand);
            abruptQuitCommand.execute();

            closeSocket();
        } catch (IOException ex) {
            handleIoException(ex);
        }
    }

    private void handleMoveJoin(ExecutableCommand inputCommand) {
        inputCommand.execute();

        String clientId = ((MoveJoinC2SCommand) inputCommand).getIdentity();

        client = STATE_MANAGER.getLocalClient(clientId);
        sender.setClientId(clientId);
    }

    private boolean handleNewIdentity(ExecutableCommand inputCommand) throws JsonProcessingException {
        Command outputCommand;
        outputCommand = inputCommand.execute();

        String clientId = ((NewIdentityC2SCommand) inputCommand).getIdentity();
        boolean isApproved = ((NewIdentityS2CCommand) outputCommand).isApproved();

        if (isApproved){
            client = STATE_MANAGER.getLocalClient(clientId);
            sender.setClientId(clientId);
        }
        sendResponse(outputCommand);
        if (isApproved) {
            Broadcaster.broadcastToAllInMainHall(
                    new RoomChangeS2CCommand(clientId, "", STATE_MANAGER.getSelf().getMainHall())
            );
            return true;
        }
        return false;
    }

    private ExecutableCommand getCommand(String json) {
        ExecutableCommand command;
        command = getExecutableCommand(json);
        setSocket(command);
        setClient(command);
        return command;
    }

    private void closeSocket() throws IOException {
        LOGGER.debug("Closing connection with client [{}] : {}", client, socket.getRemoteSocketAddress());
        socket.close();
    }

    private void sendResponse(Command outputMessage) throws JsonProcessingException {
        if (sender != null) {
            sender.send(outputMessage);
        }
    }

    private void setSocket(ExecutableCommand command) {
        if (command instanceof ClientAndSenderKnownExecutableCommand) {
            ((ClientAndSenderKnownExecutableCommand) command).setSender(sender);
        }
    }

    private void setClient(ExecutableCommand command) {
        if (command instanceof ClientKnownExecutableCommand) {
            ((ClientKnownExecutableCommand) command).setClient(client);
        }
    }

    private ExecutableCommand getExecutableCommand(String json) {
        LOGGER.debug("Received from client [{}]: {}" ,client, json);
        return C2SCommandFactory.createC2SCommand(json);
    }


    private void handleIoException(IOException ex) {
        LOGGER.error("clientserver.Server exception: " + ex.getMessage());
        ex.printStackTrace();
    }


}

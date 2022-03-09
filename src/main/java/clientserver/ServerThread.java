package clientserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import command.Command;
import command.ExecutableCommand;
import clientserver.command.clienttoserver.C2SCommandFactory;
import command.SocketExecutableCommand;
import lombok.Getter;
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

    @Getter
    private final Socket socket;

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
                    ExecutableCommand command = getExecutableCommand(json);
                    setSocket(command);
                    Command outputMessage = command.execute();
                    sendResponse(pw, outputMessage);
                }
            } catch (IOException ex) {
                handleIoException(ex);
            }
            closeSocket();
        } catch (IOException ex) {
            handleIoException(ex);
        }
    }

    private void closeSocket() throws IOException {
        LOGGER.debug("Closing connection with client : {}", socket.getRemoteSocketAddress());
        socket.close();
    }

    private void sendResponse(PrintWriter pw, Command outputMessage) throws JsonProcessingException {
        if (outputMessage != null) {
            String jsonOutputMessage = MAPPER.writeValueAsString(outputMessage);
            LOGGER.debug("Sending to client: " + jsonOutputMessage);
            pw.println(jsonOutputMessage);
        }
    }

    private void setSocket(ExecutableCommand command) {
        if (command instanceof SocketExecutableCommand) {
            ((SocketExecutableCommand) command).setSocket(socket);
        }
    }

    private ExecutableCommand getExecutableCommand(String json) {
        LOGGER.debug("Received from client: " + json);
        return C2SCommandFactory.createC2SCommand(json);
    }


    private void handleIoException(IOException ex) {
        LOGGER.error("clientserver.Server exception: " + ex.getMessage());
        ex.printStackTrace();
    }


}

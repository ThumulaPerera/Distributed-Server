package clientserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.JsonParser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientSender implements AutoCloseable{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientSender.class);
    private static final ObjectMapper MAPPER = JsonParser.getMapper();

    private BufferedWriter bw;

    public ClientSender(Socket socket) {
        try {
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean send(Command message) {
        if (bw == null) {
            LOGGER.error("BufferedWriter is null");
            return false;
        }
        if (message != null) {
            String jsonOutputMessage = null;
            try {
                jsonOutputMessage = MAPPER.writeValueAsString(message);
            } catch (JsonProcessingException e) {
                LOGGER.error("Error while serializing message", e);
                e.printStackTrace();
                return false;
            }
            LOGGER.debug("Sending to client: " + jsonOutputMessage);
            try {
                bw.write(jsonOutputMessage);
                bw.newLine(); //HERE!!!!!!
                bw.flush();
            } catch (IOException e) {
                LOGGER.error("Error while sending message", e);
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }


    @Override
    public void close(){
        try {
            LOGGER.debug("Closing ClientSender");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package clientserver.command.clienttoserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import command.CommandType;
import command.ExecutableCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.JsonParser;

import java.util.Objects;

public class C2SCommandFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(C2SCommandFactory.class);
    private static final ObjectMapper MAPPER = JsonParser.getMapper();

    public static ExecutableCommand createC2SCommand(String json) {
        String type = Objects.requireNonNull(JsonParser.parse(json)).get("type").toString();
        CommandType commandType = CommandType.getCommandType(type);

        ExecutableCommand command = null;

        try {
            switch (commandType) {
                case NEW_IDENTITY -> command = MAPPER.readValue(json, NewIdentityC2SCommand.class);
                case MESSAGE -> command = MAPPER.readValue(json, MessageC2SCommand.class);
                case LIST -> command = MAPPER.readValue(json, ListC2SCommand.class);
                case MOVE_JOIN -> command = MAPPER.readValue(json, MoveJoinC2SCommand.class);
                case QUIT -> command = MAPPER.readValue(json, QuitC2SCommand.class);
                case WHO -> command = MAPPER.readValue(json, WhoC2SCommand.class);
            }
        }
        catch (JsonProcessingException e) {
            LOGGER.error("Error while parsing json: {}", json);
            e.printStackTrace();
        }

        return command;
    }


}

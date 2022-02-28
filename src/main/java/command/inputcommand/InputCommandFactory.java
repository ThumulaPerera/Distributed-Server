package command.inputcommand;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import command.CommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.JsonParser;

import java.util.Objects;

public class InputCommandFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(InputCommandFactory.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static InputCommand createInputCommand(String json) {
        String type = Objects.requireNonNull(JsonParser.parse(json)).get("type").toString();
        CommandType commandType = CommandType.getCommandType(type);

        InputCommand inputCommand = null;

        try {
            switch (commandType) {
                case NEW_IDENTITY -> inputCommand = MAPPER.readValue(json, NewIdentityInputCommand.class);
                case MESSAGE -> inputCommand = MAPPER.readValue(json, MessageInputCommand.class);
            }
        }
        catch (JsonProcessingException e) {
            LOGGER.error("Error while parsing json: {}", json);
            e.printStackTrace();
        }

        return inputCommand;
    }


}

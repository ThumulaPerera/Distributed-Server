package serverserver.command.followertoleader;

import clientserver.command.clienttoserver.MessageC2SCommand;
import clientserver.command.clienttoserver.NewIdentityC2SCommand;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import command.CommandType;
import command.ExecutableCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.JsonParser;

import java.util.Objects;

public class F2LCommandFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(F2LCommandFactory.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static ExecutableCommand createF2LCommand(String json) {
        String type = Objects.requireNonNull(JsonParser.parse(json)).get("type").toString();
        CommandType commandType = CommandType.getCommandType(type);

        ExecutableCommand command = null;

        try {
            switch (commandType) {
                case CHECK_IDENTITY -> command = MAPPER.readValue(json, CheckIdentityF2LCommand.class);
            }
        }
        catch (JsonProcessingException e) {
            LOGGER.error("Error while parsing json: {}", json);
            e.printStackTrace();
        }

        return command;
    }


}

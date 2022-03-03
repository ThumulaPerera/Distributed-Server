package serverserver.command.leadertofollower;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import command.CommandType;
import command.ExecutableCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.command.followertoleader.CheckIdentityF2LCommand;
import utils.JsonParser;

import java.util.Objects;

public class L2FCommandFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(L2FCommandFactory.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static ExecutableCommand createL2FCommand(String json) {
        String type = Objects.requireNonNull(JsonParser.parse(json)).get("type").toString();
        CommandType commandType = CommandType.getCommandType(type);

        ExecutableCommand command = null;

        try {
            switch (commandType) {
                case CHECK_IDENTITY -> command = MAPPER.readValue(json, CheckIdentityL2FCommand.class);
            }
        }
        catch (JsonProcessingException e) {
            LOGGER.error("Error while parsing json: {}", json);
            e.printStackTrace();
        }

        return command;
    }


}

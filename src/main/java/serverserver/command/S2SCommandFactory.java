package serverserver.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import command.CommandType;
import command.ExecutableCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.command.fastbully.CoordinatorCommand;
import serverserver.command.fastbully.IamUpCommand;
import serverserver.command.fastbully.ViewCommand;
import serverserver.command.followertoleader.CheckIdentityF2LCommand;
import serverserver.command.followertoleader.HeartbeatF2LCommand;
import serverserver.command.leadertofollower.CheckIdentityL2FCommand;
import utils.JsonParser;

import java.util.Objects;

public class S2SCommandFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(S2SCommandFactory.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static ExecutableCommand createCommand(String json) {
        String type = Objects.requireNonNull(JsonParser.parse(json)).get("type").toString();
        CommandType commandType = CommandType.getCommandType(type);

        ExecutableCommand command = null;

        try {
            switch (commandType) {
                case CHECK_IDENTITY_F2L -> command = MAPPER.readValue(json, CheckIdentityF2LCommand.class);
                case CHECK_IDENTITY_L2F -> command = MAPPER.readValue(json, CheckIdentityL2FCommand.class);
                case HEARTBEAT -> command = MAPPER.readValue(json, HeartbeatF2LCommand.class);
                case IAMUP -> command = MAPPER.readValue(json, IamUpCommand.class);
                case VIEW -> command = MAPPER.readValue(json, ViewCommand.class);
                case COORDINATOR -> command = MAPPER.readValue(json, CoordinatorCommand.class);
            }
        }
        catch (JsonProcessingException e) {
            LOGGER.error("Error while parsing json: {}", json);
            e.printStackTrace();
        }

        return command;
    }


}

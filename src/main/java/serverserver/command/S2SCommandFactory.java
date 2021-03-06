package serverserver.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import command.CommandType;
import command.S2SExecutableCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.command.fastbully.*;
import serverserver.command.followertoleader.*;
import serverserver.command.leadertofollower.*;
import utils.JsonParser;

import java.util.Objects;

public class S2SCommandFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(S2SCommandFactory.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static S2SExecutableCommand createCommand(String json) {
        String type = Objects.requireNonNull(JsonParser.parse(json)).get("type").toString();
        CommandType commandType = CommandType.getCommandType(type);

        S2SExecutableCommand command = null;

        try {
            switch (commandType) {
                case CHECK_IDENTITY_F2L -> command = MAPPER.readValue(json, CheckIdentityF2LCommand.class);
                case CHECK_IDENTITY_L2F -> command = MAPPER.readValue(json, CheckIdentityL2FCommand.class);
                case ADD_ROOM_F2L -> command = MAPPER.readValue(json, AddRoomF2LCommand.class);
                case ADD_ROOM_L2F -> command = MAPPER.readValue(json, AddRoomL2FCommand.class);
                case NEW_ROOM_L2F -> command = MAPPER.readValue(json, NewRoomL2FCommand.class);
                case DELETE_ROOM_F2L -> command = MAPPER.readValue(json, DeleteRoomF2LCommand.class);
                case DELETE_ROOM_L2F -> command = MAPPER.readValue(json, DeleteRoomL2FCommand.class);
                case REMOVE_ROOM_L2F -> command = MAPPER.readValue(json, RemoveRoomL2FCommand.class);
                case HEARTBEAT -> command = MAPPER.readValue(json, HeartbeatF2LCommand.class);
                case HEARTBEAT_STATUS_CHECK -> command = MAPPER.readValue(json, HbStatusCheckL2FCommand.class);
                case HEARTBEAT_STATUS_REPLY -> command = MAPPER.readValue(json, HbStatusReplyF2LCommand.class);
                case HEARTBEAT_NOTIFY_STATUS -> command = MAPPER.readValue(json,HbStatusNotifyL2FCommand.class);
                case HEARTBEAT_ACTIVE_SERVERS -> command = MAPPER.readValue(json, HbActiveServersL2FCommand.class);
                case IAMUP -> command = MAPPER.readValue(json, IamUpCommand.class);
                case VIEW -> command = MAPPER.readValue(json, ViewCommand.class);
                case COORDINATOR -> command = MAPPER.readValue(json, CoordinatorCommand.class);
                case ELECTION -> command = MAPPER.readValue(json, ElectionCommand.class);
                case ANSWER -> command = MAPPER.readValue(json, AnswerCommand.class);
                case NOMINATION -> command = MAPPER.readValue(json, NominationCommand.class);
                case QUIT_NOTIFICATION -> command = MAPPER.readValue(json, QuitNotificationF2LCommand.class);
                case NEW_DATA -> command = MAPPER.readValue(json, NewDataF2LCommand.class);
                case SERVER_CHANGE_NOTIFICATION -> command = MAPPER.readValue(json, ServerChangeNotificationF2LCommand.class);
                case SERVER_CHANGE_NOTED -> command = MAPPER.readValue(json, ServerChangeNotedL2FCommand.class);

            }
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while parsing json: {}", json);
            e.printStackTrace();
        }

        return command;
    }


}

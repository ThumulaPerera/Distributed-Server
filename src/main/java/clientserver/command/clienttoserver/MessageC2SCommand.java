package clientserver.command.clienttoserver;

import clientserver.Broadcaster;
import clientserver.command.servertoclient.MessageS2CCommand;
import command.ClientKnownExecutableCommand;
import command.Command;
import command.CommandType;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.RefinedStateManagerImpl;
import state.StateManager;

@Getter
@Setter
public class MessageC2SCommand extends ClientKnownExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageC2SCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    private String content;

    public MessageC2SCommand() {
        super(CommandType.MESSAGE);
    }

    @Override
    public Command execute() {
        LOGGER.debug("Executing MessageInputCommand with content: {}", content);

        String selfClientId = getClient().getId();

        Command broadcastMessage = new MessageS2CCommand(selfClientId, content);

        String roomId = STATE_MANAGER.getRoomOfClient(selfClientId).getId();
        Broadcaster.broadcastToAll(roomId, broadcastMessage);

        return null;
    }

}

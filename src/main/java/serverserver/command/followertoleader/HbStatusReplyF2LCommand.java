package serverserver.command.followertoleader;

import command.Command;
import command.CommandType;
import command.ExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.StateManager;
import state.StateManagerImpl;

@Getter
@Setter
public class HbStatusReplyF2LCommand extends ExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(HbStatusReplyF2LCommand.class);
    private static final StateManager STATE_MANAGER = StateManagerImpl.getInstance();

    private String from;

    public HbStatusReplyF2LCommand() {
        super(CommandType.HEARTBEAT_STATUS_REPLY);
    }

    public HbStatusReplyF2LCommand(String serverID) {
        this();
        this.from = serverID;
    }

    @Override
    public Command execute() {
        LOGGER.debug("Executing Heartbeat status check from Leader");
        //TODO Evaluate and change the avl status
        return null;
    }
}

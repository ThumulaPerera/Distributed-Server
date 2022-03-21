package serverserver.command.followertoleader;

import command.Command;
import command.CommandType;
import command.S2SExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.RefinedStateManagerImpl;
import state.StateManager;

@Getter
@Setter
public class HbStatusReplyF2LCommand extends S2SExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(HbStatusReplyF2LCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

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
        LOGGER.debug("Executing Heartbeat status reply to Leader");
        STATE_MANAGER.getHeartbeatDetector().handleHbStatusReply(from);
        return null;
    }
}

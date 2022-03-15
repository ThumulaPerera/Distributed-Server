package serverserver.command.leadertofollower;

import command.Command;
import command.CommandType;
import command.S2SExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.command.followertoleader.HbStatusReplyF2LCommand;
import state.RefinedStateManagerImpl;
import state.StateManager;

@Getter
@Setter
public class HbStatusCheckL2FCommand extends S2SExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(HbStatusCheckL2FCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    public HbStatusCheckL2FCommand() {
        super(CommandType.HEARTBEAT_STATUS_CHECK);
    }


    @Override
    public Command execute() {
        LOGGER.debug("Executing Heartbeat status check from Leader");
        return new HbStatusReplyF2LCommand(STATE_MANAGER.getSelf().getId());
    }
}

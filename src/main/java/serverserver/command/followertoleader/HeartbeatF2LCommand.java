package serverserver.command.followertoleader;

import command.Command;
import command.CommandType;
import command.ExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.command.leadertofollower.CheckIdentityL2FCommand;
import state.StateManager;
import state.StateManagerImpl;

@Getter
@Setter
public class HeartbeatF2LCommand extends ExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatF2LCommand.class);
    private static final StateManager STATE_MANAGER = StateManagerImpl.getInstance();

    private String from;

    public HeartbeatF2LCommand() {
        super(CommandType.HEARTBEAT);
    }

    public HeartbeatF2LCommand(String serverID) {
        this();
        this.from = serverID;
    }

    @Override
    public Command execute() {
        LOGGER.debug("Executing Heartbeat from Server {}", from);
        STATE_MANAGER.getLeader().getHeartbeatDetector().updateTime(from,System.currentTimeMillis());
        //TODO send the ack message
        return null;
    }
}

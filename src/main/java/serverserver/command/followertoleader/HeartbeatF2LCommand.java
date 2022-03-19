package serverserver.command.followertoleader;

import command.Command;
import command.CommandType;
import command.S2SExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.command.leadertofollower.HbActiveServersL2FCommand;
import state.RefinedStateManagerImpl;
import state.StateManager;

@Getter
@Setter
public class HeartbeatF2LCommand extends S2SExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatF2LCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

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
        STATE_MANAGER.getLeader().getHeartbeatDetector().updateTime(from,System.currentTimeMillis());
        return new HbActiveServersL2FCommand(STATE_MANAGER.getLeader().getHeartbeatDetector().getActiveServers());
    }
}

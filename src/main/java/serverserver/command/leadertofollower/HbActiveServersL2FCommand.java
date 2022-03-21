package serverserver.command.leadertofollower;

import command.Command;
import command.CommandType;
import command.S2SExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.RefinedStateManagerImpl;
import state.ServerAvailability;
import state.ServerModel;
import state.StateManager;

import java.util.Set;

@Getter
@Setter
public class HbActiveServersL2FCommand extends S2SExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(HbActiveServersL2FCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    public HbActiveServersL2FCommand() {
        super(CommandType.HEARTBEAT_ACTIVE_SERVERS);
    }

    private Set<String> serverlist;

    public HbActiveServersL2FCommand(Set<String> servers) {
        this();
        this.serverlist = servers;
    }


    @Override
    public Command execute() {
        STATE_MANAGER.updateAvailableServersList(serverlist);
        STATE_MANAGER.addAvailableServerId(this.getOrigin());
        return null;
    }
}

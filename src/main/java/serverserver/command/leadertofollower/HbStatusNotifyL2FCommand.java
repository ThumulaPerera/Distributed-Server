package serverserver.command.leadertofollower;

import command.Command;
import command.CommandType;
import command.S2SExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.RefinedStateManagerImpl;
import state.StateManager;

import java.util.Set;

@Getter
@Setter
public class HbStatusNotifyL2FCommand extends S2SExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(HbStatusNotifyL2FCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    public HbStatusNotifyL2FCommand() {
        super(CommandType.HEARTBEAT_NOTIFY_STATUS);
    }

    private String inactiveServer;

    public HbStatusNotifyL2FCommand(String serverID) {
        this();
        this.inactiveServer = serverID;
    }


    @Override
    public Command execute() {
        STATE_MANAGER.removeAvailableServerId(this.inactiveServer);
        STATE_MANAGER.removeAllChatRoomsOfRemoteServer(this.inactiveServer);
        STATE_MANAGER.removeClientsOfRemoteServer(this.inactiveServer);
        return null;
    }
}

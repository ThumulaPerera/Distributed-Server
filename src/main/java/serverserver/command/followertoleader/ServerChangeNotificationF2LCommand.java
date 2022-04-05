package serverserver.command.followertoleader;

import command.Command;
import command.CommandType;
import command.S2SExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.command.leadertofollower.ServerChangeNotedL2FCommand;
import state.RefinedStateManagerImpl;
import state.StateManager;

@Getter
@Setter
public class ServerChangeNotificationF2LCommand extends S2SExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerChangeNotificationF2LCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    private String clientid;

    public ServerChangeNotificationF2LCommand() {
        super(CommandType.SERVER_CHANGE_NOTIFICATION);
    }

    public ServerChangeNotificationF2LCommand(String clientid) {
        this();
        this.clientid = clientid;
    }

    @Override
    public Command execute() {
        if (STATE_MANAGER.isLeader()){
            STATE_MANAGER.changeServerOfClient(clientid, getOrigin());
            return new ServerChangeNotedL2FCommand(true);
        }
        return null;
    }
}

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
import state.ServerAvailability;

@Getter
@Setter
public class HbStatusNotifyL2FCommand extends S2SExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(HbStatusNotifyL2FCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    public HbStatusNotifyL2FCommand() {
        super(CommandType.HEARTBEAT_STATUS_NOTIFY);
    }

    private String serverid;
    private ServerAvailability status;

    public HbStatusNotifyL2FCommand(String serverID, ServerAvailability serverStatus) {
        this();
        this.serverid = serverID;
        this.status = serverStatus;
    }


    @Override
    public Command execute() {
        if(status == ServerAvailability.ACTIVE){
            STATE_MANAGER.addAvailableServerId(serverid);
        }else if(status == ServerAvailability.INACTIVE){
            STATE_MANAGER.removeAvailableServerId(serverid);
        }
        return null;
    }
}

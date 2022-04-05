package serverserver.command.fastbully;

import command.Command;
import command.CommandType;
import command.S2SExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.RefinedStateManagerImpl;
import state.StateManager;

import java.util.List;


public class ViewCommand extends S2SExecutableCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();
    @Getter @Setter private List<String> availableServers;
    @Getter @Setter private String from;
    public ViewCommand() {
        super(CommandType.VIEW);
    }

    @Override
    //executed when view is received
    public Command execute() {
        // TODO: decide whether to enable this
//        for(String peerId: availableServers){
//            STATE_MANAGER.addAvailableServer(peerId);
//        }
        STATE_MANAGER.addAvailableServerId(from);
        // TODO: probably we need heartbeat between each server to check status of each other
        return null;
    }
}

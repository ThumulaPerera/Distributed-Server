package serverserver.command.fastbully;

import command.Command;
import command.CommandType;
import command.ExecutableCommand;
import command.S2SExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.StateManager;
import state.StateManagerImpl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ViewCommand extends S2SExecutableCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewCommand.class);
    private static final StateManagerImpl STATE_MANAGER = StateManagerImpl.getInstance();
    @Getter @Setter private Set<String> availableServers;
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
        STATE_MANAGER.addAvailableServer(from);
        // TODO: probably we need heartbeat between each server to check status of each other
        return null;
    }
}

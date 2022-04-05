package serverserver.command.fastbully;

import command.Command;
import command.CommandType;
import command.S2SExecutableCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.RefinedStateManagerImpl;
import state.StateManager;
import java.util.List;


public class IamUpCommand extends S2SExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(IamUpCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    public IamUpCommand() {
        super(CommandType.IAMUP);
    }

    @Override
    public Command execute() {
        ViewCommand command = new ViewCommand();
        List<String> availableServers = STATE_MANAGER.getAvailableServerIds();

        command.setAvailableServers(availableServers);
        command.setFrom(STATE_MANAGER.getSelf().getId());

        return command;
    }
}

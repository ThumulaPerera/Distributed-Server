package serverserver.command.fastbully;

import command.Command;
import command.CommandType;
import command.ExecutableCommand;
import command.S2SExecutableCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.StateManagerImpl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class IamUpCommand extends S2SExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(IamUpCommand.class);

    public IamUpCommand() {
        super(CommandType.IAMUP);
    }

    @Override
    public Command execute() {
        ViewCommand command = new ViewCommand();
        Set<String> availableServers = StateManagerImpl.getInstance().getAvailableServers();

        Set<String> avlServersCpy = new HashSet<String>();
        synchronized (availableServers) {
            Iterator<String> iterator = availableServers.iterator();
            while (iterator.hasNext()) {
                avlServersCpy.add(iterator.next());
            }
        }
        command.setAvailableServers(avlServersCpy);

        command.setFrom(StateManagerImpl.getInstance().getSelf().getId());
        return command;
    }
}

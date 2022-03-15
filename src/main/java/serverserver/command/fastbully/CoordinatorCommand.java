package serverserver.command.fastbully;

import command.Command;
import command.CommandType;
import command.S2SExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import state.RefinedStateManagerImpl;
import state.StateManager;

public class CoordinatorCommand extends S2SExecutableCommand {
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    @Getter @Setter private String from;
    public CoordinatorCommand() {
        super(CommandType.COORDINATOR);
    }

    @Override
    public Command execute() {
        STATE_MANAGER.setLeader(from);
        STATE_MANAGER.setElectionAllowed(false);
        return null;
    }
}

package serverserver.command.fastbully;

import command.Command;
import command.CommandType;
import command.ExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import state.StateManagerImpl;

public class CoordinatorCommand extends ExecutableCommand {
    @Getter @Setter private String from;
    public CoordinatorCommand() {
        super(CommandType.COORDINATOR);
    }

    @Override
    public Command execute() {
        StateManagerImpl STATE_MANAGER = StateManagerImpl.getInstance();
        STATE_MANAGER.setLeader(from);
        STATE_MANAGER.setElectionAllowed(false);
        return null;
    }
}

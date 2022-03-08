package serverserver.command.fastbully;

import command.Command;
import command.CommandType;
import command.ExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import serverserver.FastBully;
import state.StateManagerImpl;

public class NominationCommand extends ExecutableCommand {
    @Getter @Setter private String from;
    public NominationCommand() {
        super(CommandType.NOMINATION);
    }

    @Override
    public Command execute() {
        StateManagerImpl STATE_MANAGER = StateManagerImpl.getInstance();
        CoordinatorCommand coordinatorCommand = new CoordinatorCommand();
        coordinatorCommand.setFrom(STATE_MANAGER.getSelf().getId());
        STATE_MANAGER.setElectionAllowed(false);
        STATE_MANAGER.setLeader(STATE_MANAGER.getSelf().getId());
        FastBully.sendCoordinatorCommandToOtherLowerPeers(coordinatorCommand, this.from);
        return coordinatorCommand;
    }
}

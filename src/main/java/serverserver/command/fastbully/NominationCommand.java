package serverserver.command.fastbully;

import command.Command;
import command.CommandType;
import command.S2SExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import serverserver.FastBully;
import state.RefinedStateManagerImpl;
import state.StateManager;

import java.util.stream.Collectors;

public class NominationCommand extends S2SExecutableCommand {
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    @Getter @Setter private String from;
    public NominationCommand() {
        super(CommandType.NOMINATION);
    }

    @Override
    public Command execute() {
        // TODO: Since now I am the new leader,
        //  copy my local room data and client data to all room data and all client data (in STATE_MANAGER)
        STATE_MANAGER.addClientData(
                STATE_MANAGER
                    .getAllLocalClients()
                    .stream()
                    .map(client -> client.getId()).toList()
        );

        CoordinatorCommand coordinatorCommand = new CoordinatorCommand();
        coordinatorCommand.setFrom(STATE_MANAGER.getSelf().getId());
        STATE_MANAGER.setElectionAllowed(false);
        STATE_MANAGER.setLeader(STATE_MANAGER.getSelf().getId());
        FastBully.sendCoordinatorCommandToOtherLowerPeers(coordinatorCommand, this.from);
        return coordinatorCommand;
    }
}

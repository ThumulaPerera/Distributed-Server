package command;

import lombok.Getter;
import lombok.Setter;
import state.RefinedStateManagerImpl;
import state.StateManager;

@Getter
@Setter
public abstract class S2SExecutableCommand extends ExecutableCommand {
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();
    private String origin;

    public S2SExecutableCommand(CommandType type) {
        super(type);
        this.origin = STATE_MANAGER.getSelf().getId();
    }
}

package command;

import lombok.Getter;
import lombok.Setter;
import state.StateManager;
import state.StateManagerImpl;

@Getter
@Setter
public abstract class S2SExecutableCommand extends ExecutableCommand {
    private static final StateManager STATE_MANAGER = StateManagerImpl.getInstance();
    private String origin;

    public S2SExecutableCommand(CommandType type) {
        super(type);
        this.origin = STATE_MANAGER.getSelf().getId();
    }
}

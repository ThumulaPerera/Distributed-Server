package serverserver.command.followertoleader;

import command.Command;
import command.CommandType;
import command.S2SExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.command.leadertofollower.CheckIdentityL2FCommand;
import state.RefinedStateManagerImpl;
import state.StateManager;

@Getter
@Setter
public class CheckIdentityF2LCommand extends S2SExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckIdentityF2LCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    private String identity;

    public CheckIdentityF2LCommand() {
        super(CommandType.CHECK_IDENTITY_F2L);
    }

    public CheckIdentityF2LCommand(String identity) {
        this();
        this.identity = identity;
    }

    @Override
    public Command execute() {
        // executed by leader only

        LOGGER.debug("Executing Check Identity F2L with identity: {}", identity);

        return new CheckIdentityL2FCommand(identity, isIdentityValid());
    }

    private boolean isIdentityValid() {
        return STATE_MANAGER.checkAvailabilityAndAddGlobalClient(identity, getOrigin());
    }
}

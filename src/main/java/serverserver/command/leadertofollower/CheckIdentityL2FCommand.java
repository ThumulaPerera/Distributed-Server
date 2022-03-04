package serverserver.command.leadertofollower;

import command.Command;
import command.CommandType;
import command.ExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
public class CheckIdentityL2FCommand extends ExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckIdentityL2FCommand.class);

    private String identity;
    private boolean approved;

    public CheckIdentityL2FCommand() {
        super(CommandType.CHECK_IDENTITY_L2F);
    }

    public CheckIdentityL2FCommand(String identity, boolean approved) {
        this();
        this.identity = identity;
        this.approved = approved;
    }

    @Override
    public Command execute() {
        LOGGER.debug("Executing Check Identity L2F with identity: {}", identity);
        return this;
    }
}

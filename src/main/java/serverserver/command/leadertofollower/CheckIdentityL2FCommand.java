package serverserver.command.leadertofollower;

import command.Command;
import command.CommandType;
import command.ExecutableCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckIdentityL2FCommand extends ExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckIdentityL2FCommand.class);

    private String identity;
    private boolean approved;

    public CheckIdentityL2FCommand() {
        super(CommandType.CHECK_IDENTITY);
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

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}

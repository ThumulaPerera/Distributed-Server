package clientserver.command.clienttoserver;

import command.CommandType;
import command.ExecutableCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewIdentityC2SCommand extends ExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewIdentityC2SCommand.class);

    private String identity;

    public NewIdentityC2SCommand() {
        super(CommandType.NEW_IDENTITY);
    }

    @Override
    public void execute() {
        LOGGER.info("Executing NewIdentityInputCommand with identity: {}", identity);
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}

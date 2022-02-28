package command.inputcommand;

import command.CommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewIdentityInputCommand extends InputCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewIdentityInputCommand.class);

    private String identity;

    public NewIdentityInputCommand() {
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

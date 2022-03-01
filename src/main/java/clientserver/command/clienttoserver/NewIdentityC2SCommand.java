package clientserver.command.clienttoserver;

import clientserver.command.servertoclient.NewIdentityS2CCommand;
import command.Command;
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
    public Command execute() {
        LOGGER.debug("Executing NewIdentityInputCommand with identity: {}", identity);

        return new NewIdentityS2CCommand(isIdentityValid());
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    private boolean isIdentityValid() {
        // TODO: implement
        return true;
    }
}

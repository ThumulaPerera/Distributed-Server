package clientserver.command.clienttoserver;

import clientserver.command.servertoclient.NewIdentityS2CCommand;
import command.Command;
import command.CommandType;
import command.ExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
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

    private boolean isIdentityValid() {
        // TODO: implement
        return true;
    }
}

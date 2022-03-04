package serverserver.command.followertoleader;

import clientserver.command.clienttoserver.NewIdentityC2SCommand;
import command.Command;
import command.CommandType;
import command.ExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.command.leadertofollower.CheckIdentityL2FCommand;

@Getter
@Setter
public class CheckIdentityF2LCommand extends ExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckIdentityF2LCommand.class);

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
        // TODO: check if identity is in global user identities list
        return true;
    }
}

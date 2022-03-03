package clientserver.command.clienttoserver;

import clientserver.command.servertoclient.NewIdentityS2CCommand;
import command.Command;
import command.CommandType;
import command.ExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.Sender;
import serverserver.command.followertoleader.CheckIdentityF2LCommand;
import serverserver.command.leadertofollower.CheckIdentityL2FCommand;
import state.StateManager;
import state.StateManagerImpl;

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
        LOGGER.debug("Executing client request for new identity with identity: {}", identity);

        return new NewIdentityS2CCommand(isIdentityValid());
    }

    private boolean isIdentityValid() {
        Sender sender = new Sender();
        StateManager stateManager = StateManagerImpl.getInstance();
        if (stateManager.isLeader()){
            // TODO: implement
            return false;
        } else {
            Command response = sender.sendCommandToLeader(new CheckIdentityF2LCommand(identity));
            if (response instanceof CheckIdentityL2FCommand) {
                return ((CheckIdentityL2FCommand) response).isApproved();
            }
        }
        return false;
    }
}

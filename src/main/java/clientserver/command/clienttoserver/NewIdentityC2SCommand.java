package clientserver.command.clienttoserver;

import clientserver.command.servertoclient.NewIdentityS2CCommand;
import command.Command;
import command.CommandType;
import command.SenderKnownExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.Sender;
import serverserver.command.followertoleader.CheckIdentityF2LCommand;
import serverserver.command.leadertofollower.CheckIdentityL2FCommand;
import state.RefinedStateManagerImpl;
import state.StateManager;

@Getter
@Setter
public class NewIdentityC2SCommand extends SenderKnownExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewIdentityC2SCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    private String identity;

    public NewIdentityC2SCommand() {
        super(CommandType.NEW_IDENTITY);
    }

    @Override
    public Command execute() {
        LOGGER.debug("Executing client request for new identity with identity: {}", identity);

        if(!isValid()){
            LOGGER.debug("Invalid identity: {}", identity);
            return new NewIdentityS2CCommand(false);
        }

        boolean isAvailable = isAvailable();

        return new NewIdentityS2CCommand(isAvailable);
    }

    private boolean isAvailable() {
        boolean isAvailable = false;
        if (STATE_MANAGER.isLeader()){
            isAvailable = STATE_MANAGER.checkAvailabilityAndAddNewLocalClient(identity, getSender());
        } else {
            // check local client list first
            if(!STATE_MANAGER.isIdLocallyAvailable(identity)) return false;
            // if not locally available, check with leader
            Command response = Sender.sendCommandToLeaderAndReceive(new CheckIdentityF2LCommand(identity));
            if (response instanceof CheckIdentityL2FCommand) {
                isAvailable = ((CheckIdentityL2FCommand) response).isApproved();
                if (isAvailable) {
                    STATE_MANAGER.addNewLocalClient(identity, getSender());
                }
            }
        }
        return isAvailable;
    }

    private boolean isValid(){
        /*
          matches with
            - starting with lower or upper case letter
            - containing only lower or upper case letters and numbers
            - not longer than 16 characters
            - not shorter than 3 characters
         */
        String regex = "^[a-zA-Z][a-zA-Z0-9]{2,15}$";

        return identity.matches(regex);
    }

}

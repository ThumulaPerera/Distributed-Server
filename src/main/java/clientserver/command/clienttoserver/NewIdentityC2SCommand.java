package clientserver.command.clienttoserver;

import clientserver.command.servertoclient.NewIdentityS2CCommand;
import command.Command;
import command.CommandType;
import command.ExecutableCommand;
import command.SocketExecutableCommand;
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
public class NewIdentityC2SCommand extends SocketExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewIdentityC2SCommand.class);
    private static final StateManager STATE_MANAGER = StateManagerImpl.getInstance();

    private String identity;

    public NewIdentityC2SCommand() {
        super(CommandType.NEW_IDENTITY);
    }

    @Override
    public Command execute() {
        LOGGER.debug("Executing client request for new identity with identity: {}", identity);

        boolean isValid = false;
        if (STATE_MANAGER.isLeader()){
            isValid = STATE_MANAGER.checkValidityAndAddLocalClient(identity, getSocket());
        } else {
            Command response = Sender.sendCommandToLeaderAndReceive(new CheckIdentityF2LCommand(identity));
            if (response instanceof CheckIdentityL2FCommand) {
                isValid = ((CheckIdentityL2FCommand) response).isApproved();
                if (isValid) {
                    STATE_MANAGER.addLocalClient(identity, getSocket());
                }
            }
        }

        return new NewIdentityS2CCommand(isValid);
    }

//    private boolean isIdentityValid() {
//        Sender sender = new Sender();
//        StateManager stateManager = StateManagerImpl.getInstance();
//        if (stateManager.isLeader()){
//            return STATE_MANAGER.checkValidityAndAddClient(identity, STATE_MANAGER.getSelf().getId());
//        } else {
//            Command response = sender.sendCommandToLeaderAndReceive(new CheckIdentityF2LCommand(identity));
//            if (response instanceof CheckIdentityL2FCommand) {
//                return ((CheckIdentityL2FCommand) response).isApproved();
//            }
//        }
//        return false;
//    }
}

package clientserver.command.clienttoserver;

import clientserver.command.servertoclient.CreateRoomS2CCommand;
import command.Command;
import command.CommandType;
import command.ExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.Sender;
import serverserver.command.followertoleader.AddRoomF2LCommand;
import serverserver.command.leadertofollower.AddRoomL2FCommand;
import serverserver.command.leadertofollower.CheckIdentityL2FCommand;
import state.StateManager;
import state.StateManagerImpl;

@Getter
@Setter
public class CreateRoomC2SCommand extends ExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewIdentityC2SCommand.class);
    private static final StateManager STATE_MANAGER = StateManagerImpl.getInstance();

    private String roomid;

    public CreateRoomC2SCommand() {
        super(CommandType.CREATE_ROOM);
    }

    @Override
    public Command execute() {
        LOGGER.debug("Executing client request for create room with identity: {}", roomid);

        return new CreateRoomS2CCommand(checkAndAddRoom(),roomid);
    }

    private boolean checkAndAddRoom() {
        Sender sender = new Sender();
        // TODO : remove hardcoded clientId
        String clientId = "client-1";
        if (STATE_MANAGER.getSelf().containsChatRoom(roomid)) {
            return false;
        } else {
            LOGGER.debug("===isLeader: {}", STATE_MANAGER.isLeader());
            LOGGER.debug("===server : {}", STATE_MANAGER.getSelf().getId());
            if (STATE_MANAGER.isLeader()) {
                LOGGER.debug("=================== leader ");
                boolean isAdded = STATE_MANAGER.checkValidityAndAddRoom(roomid, STATE_MANAGER.getSelf().getId(), clientId);

                if (isAdded) {
                    LOGGER.debug("=================== leader: added room ");
                    //            TODO: Send newroom to all the servers except the origin server
                }
                return isAdded;
            } else {
                LOGGER.debug("===================not leader ");
                AddRoomF2LCommand addRoomCmnd = new AddRoomF2LCommand(roomid, clientId);
                LOGGER.debug(addRoomCmnd.toString());
                Command response = sender.sendCommandToLeaderAndReceive(addRoomCmnd);
                LOGGER.debug("=================== response {}", response.getType());
                if (response instanceof AddRoomL2FCommand) {
                    return ((AddRoomL2FCommand) response).isApproved();
                }
            }
            return false;
        }


    }
}

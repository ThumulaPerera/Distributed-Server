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

        // TODO : remove hardcoded clientId
        String clientId = "client-1";
        boolean isApproved = checkAndAddRoom(clientId);
        if (isApproved) {
            deleteClientOwnRoom(clientId);
            joinRoom(clientId);
        }
        return new CreateRoomS2CCommand(isApproved, roomid);
    }

    private void deleteClientOwnRoom(String clientId) {
        // TODO: check the local rooms which has client as owner and delete room if exists
    }

    private void joinRoom(String clientId) {
        // TODO: Leave the current the room if in a room
        // TODO: Join the room
    }

    private boolean checkAndAddRoom(String clientId) {
        Sender sender = new Sender();

        if (STATE_MANAGER.getSelf().containsChatRoom(roomid)) {
            return false;
        } else {
            if (STATE_MANAGER.isLeader()) {
                return STATE_MANAGER.checkValidityAndAddRoom(roomid, STATE_MANAGER.getSelf().getId(), clientId);

            } else {
                AddRoomF2LCommand addRoomCmnd = new AddRoomF2LCommand(roomid, clientId);
                LOGGER.debug(addRoomCmnd.toString());
                Command response = sender.sendCommandToLeaderAndReceive(addRoomCmnd);
                if (response instanceof AddRoomL2FCommand) {
                    return ((AddRoomL2FCommand) response).isApproved();
                }
            }
            return false;
        }


    }
}

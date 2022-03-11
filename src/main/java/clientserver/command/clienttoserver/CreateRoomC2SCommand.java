package clientserver.command.clienttoserver;

import clientserver.command.servertoclient.CreateRoomS2CCommand;
import command.ClientKnownExecutableCommand;
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
import state.ChatRoomModel;
import state.StateManager;
import state.StateManagerImpl;

@Getter
@Setter
public class CreateRoomC2SCommand extends ClientKnownExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewIdentityC2SCommand.class);
    private static final StateManager STATE_MANAGER = StateManagerImpl.getInstance();

    private String roomid;

    public CreateRoomC2SCommand() {
        super(CommandType.CREATE_ROOM);
    }

    @Override
    public Command execute() {
        LOGGER.debug("Executing client request for create room with identity: {}", roomid);

        String clientId = getClient().getId();
        LOGGER.debug("================== Creating Room for Client: {}", clientId);
        String currentOwnedRoom = STATE_MANAGER.getSelf().getChatRoomByOwner(clientId);
        boolean isApproved;
        if (currentOwnedRoom == null) {
            isApproved = checkAndAddRoom(clientId);
            if (isApproved) {
                joinRoom(clientId, roomid);
            }
        } else {
            isApproved = checkAndAddRoom(clientId);
            if (isApproved) {
                // TODO: move others in currentOwnedRoom to Mainhall
                // TODO: delete currentOwnedRoom
                joinRoom(clientId, roomid);
            }
        }

        return new CreateRoomS2CCommand(isApproved, roomid);
    }

    private void deleteRoom(String roomId) {


    }

    private void joinRoom(String clientId, String roomid) {
        // TODO: JoinRoom
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

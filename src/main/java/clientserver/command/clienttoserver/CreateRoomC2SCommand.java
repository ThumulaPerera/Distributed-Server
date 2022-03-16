package clientserver.command.clienttoserver;

import clientserver.command.servertoclient.CreateRoomS2CCommand;
import command.ClientKnownExecutableCommand;
import command.Command;
import command.CommandType;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.Sender;
import serverserver.command.followertoleader.AddRoomF2LCommand;
import serverserver.command.leadertofollower.AddRoomL2FCommand;
import serverserver.command.leadertofollower.NewRoomL2FCommand;
import state.RefinedStateManagerImpl;
import state.StateManager;

@Getter
@Setter
public class CreateRoomC2SCommand extends ClientKnownExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewIdentityC2SCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    private String roomid;

    public CreateRoomC2SCommand() {
        super(CommandType.CREATE_ROOM);
    }

    @Override
    public Command execute() {
        LOGGER.debug("Executing client request for create room with identity: {}", roomid);

        if(!isValid()){
            return new CreateRoomS2CCommand(false, roomid);
        }

        if(alreadyOwnsRoom()){
            return new CreateRoomS2CCommand(false, roomid);
        }

        boolean isApproved = checkAndAddRoom();

        if (isApproved) {
            joinRoom();
        }

        return new CreateRoomS2CCommand(isApproved, roomid);
    }


    private void joinRoom() {
        // TODO: JoinRoom
    }

    private boolean checkAndAddRoom() {
        boolean isAdded = false;

        if (STATE_MANAGER.isRoomIdAvailable(roomid)) { // room does not exist according to the locally maintained list of all room ids
            if (STATE_MANAGER.isLeader()) {
                isAdded = STATE_MANAGER.checkValidityAndAddLocalRoom(roomid, getClient());
                if (isAdded) {
                    NewRoomL2FCommand newRoomL2FCommand = new NewRoomL2FCommand(
                            roomid,
                            STATE_MANAGER.getSelf().getId()
                    );
                    Sender.broadcastCommandToAllFollowers(newRoomL2FCommand);
                }

            } else {
                AddRoomF2LCommand addRoomF2LCommand = new AddRoomF2LCommand(roomid);
                Command response = Sender.sendCommandToLeaderAndReceive(addRoomF2LCommand);
                if (response instanceof AddRoomL2FCommand) {
                    boolean isApproved = ((AddRoomL2FCommand) response).isApproved();
                    if (isApproved) {
                        STATE_MANAGER.addLocalRoom(roomid, getClient());
                        isAdded = true;
                    }
                }
            }
        }
        return isAdded;
    }

    private boolean alreadyOwnsRoom(){
        return STATE_MANAGER.getRoomOwnedByClient(getClient().getId())!= null;
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

        return roomid.matches(regex);
    }
}

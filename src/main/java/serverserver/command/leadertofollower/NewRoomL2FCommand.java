package serverserver.command.leadertofollower;

import command.Command;
import command.CommandType;
import command.S2SExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.ChatRoomModel;
import state.ClientModel;
import state.StateManager;
import state.StateManagerImpl;

@Getter
@Setter
public class NewRoomL2FCommand extends S2SExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewRoomL2FCommand.class);

    private String roomid;
    private String ownerclient;
    private String ownerserver;

    public NewRoomL2FCommand() {
        super(CommandType.NEW_ROOM_L2F);
    }

    public NewRoomL2FCommand(String roomid, String ownerclient, String ownerserver) {
        this();
        this.roomid = roomid;
        this.ownerclient = ownerclient;
        this.ownerserver = ownerserver;

    }

    @Override
    public Command execute() {
        LOGGER.debug("Executing New Room L2F with roomid: {}, ownerclient: {}, ownerserver: {}", roomid, ownerclient, ownerserver);

        // Save room locally
        StateManager STATE_MANAGER = StateManagerImpl.getInstance();
        STATE_MANAGER.getSelf().addChatRoom(new ChatRoomModel(roomid, new ClientModel(ownerclient), STATE_MANAGER.getServer(ownerserver)));


        return this;
    }
}

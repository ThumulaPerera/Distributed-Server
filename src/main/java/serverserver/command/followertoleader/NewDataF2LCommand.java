package serverserver.command.followertoleader;

import command.Command;
import command.CommandType;
import command.S2SExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.RefinedStateManagerImpl;
import state.StateManager;

import java.util.List;

/*
Command sent after receiving coordinator command.
This command sends client list and chatroom list so the new leader can continue the work
 */
public class NewDataF2LCommand extends S2SExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckIdentityF2LCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    @Getter @Setter
    private List<String> chatRooms;
    @Getter @Setter
    private List<String> clients;

    public NewDataF2LCommand() {
        super(CommandType.NEW_DATA);
    }

    @Override
    public Command execute() {
        STATE_MANAGER.addClientData(clients, getOrigin());
        STATE_MANAGER.addChatroomData(chatRooms, getOrigin());
        return null;
    }
}

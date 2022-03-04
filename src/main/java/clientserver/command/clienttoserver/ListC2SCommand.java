package clientserver.command.clienttoserver;

import clientserver.command.servertoclient.ListS2CCommand;
import command.Command;
import command.CommandType;
import command.ExecutableCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.Getter;
import lombok.Setter;
import state.ChatRoomModel;
import state.ServerModel;
import state.StateManager;
import state.StateManagerImpl;

import java.util.ArrayList;
import java.util.Map;

@Getter
@Setter
public class ListC2SCommand extends ExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListC2SCommand.class);

    private String content;

    public ListC2SCommand() {
        super(CommandType.LIST);
    }

    @Override
    public Command execute() {
        LOGGER.debug("Executing ListInputCommand with content");
        return new ListS2CCommand(getRoomList());
    }

    private ArrayList<String> getRoomList() {
        ArrayList<String> rooms = new ArrayList<String>();
        StateManager stateManager = StateManagerImpl.getInstance();
        ServerModel myServer = stateManager.getSelf();

        Map<String, ChatRoomModel> chatRooms = myServer.getChatRooms();
        for (var entry : chatRooms.entrySet()) {
            rooms.add(entry.getKey());
        }

//        TODO: Remove hardcoded rooms
        rooms.add("A");
        rooms.add("B");
        rooms.add("C");
        return rooms;
    }

}
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
import state.RefinedStateManagerImpl;
import state.StateManager;

import java.util.List;

@Getter
@Setter
public class ListC2SCommand extends ExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListC2SCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    private String content;

    public ListC2SCommand() {
        super(CommandType.LIST);
    }

    @Override
    public Command execute() {
        LOGGER.debug("Executing ListInputCommand");
        return new ListS2CCommand(getRoomList());
    }

    private List<String> getRoomList() {
        return STATE_MANAGER.getAllAvailableChatRooms().stream().map(ChatRoomModel::getId).toList();
    }

}
package clientserver.command.clienttoserver;

import clientserver.command.servertoclient.ListS2CCommand;
import clientserver.command.servertoclient.NewIdentityS2CCommand;
import command.Command;
import command.CommandType;
import command.ExecutableCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

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
//        TODO: Get all the available rooms
        rooms.add("A");
        rooms.add("B");
        rooms.add("C");
        return rooms;
    }

}
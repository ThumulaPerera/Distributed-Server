package clientserver.command.servertoclient;

import command.Command;
import command.CommandType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListS2CCommand extends Command {
    private List<String> rooms;

    public ListS2CCommand() {
        super(CommandType.ROOM_LIST);
    }

    public ListS2CCommand(List<String> rooms) {
        this();
        this.rooms = rooms;
    }

}

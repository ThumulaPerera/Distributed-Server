
package clientserver.command.servertoclient;

import command.Command;
import command.CommandType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class ListS2CCommand extends Command {
    private ArrayList<String> rooms;

    public ListS2CCommand() {
        super(CommandType.LIST);
    }

    public ListS2CCommand(ArrayList<String> rooms) {
        this();
        this.rooms = rooms;
    }

}

package clientserver.command.servertoclient;

import command.Command;
import command.CommandType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomChangeS2CCommand extends Command {
    private String identity;
    private String former;
    private String roomid;

    public RoomChangeS2CCommand() {
        super(CommandType.ROOM_CHANGE);
    }

    public RoomChangeS2CCommand(String identity, String former, String roomid) {
        this();
        this.identity = identity;
        this.former = former;
        this.roomid = roomid;
    }
}

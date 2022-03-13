package serverserver.command.leadertofollower;

import command.Command;
import command.CommandType;
import command.S2SExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
public class AddRoomL2FCommand extends S2SExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddRoomL2FCommand.class);

    private String roomid;
    private boolean approved;

    public AddRoomL2FCommand() {
        super(CommandType.ADD_ROOM_L2F);
    }

    public AddRoomL2FCommand(String roomid, boolean approved) {
        this();
        this.roomid = roomid;
        this.approved = approved;
    }

    @Override
    public Command execute() {
        LOGGER.debug("Executing Add Room L2F with identity: {}", roomid);
        return this;
    }
}

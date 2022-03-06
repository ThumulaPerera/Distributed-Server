package clientserver.command.servertoclient;

import command.Command;
import command.CommandType;
import command.ExecutableCommand;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewIdentityS2CCommand extends ExecutableCommand {
    private boolean approved;

    public NewIdentityS2CCommand() {
        super(CommandType.NEW_IDENTITY);
    }

    public NewIdentityS2CCommand(boolean approved) {
        this();
        this.approved = approved;
    }

    @Override
    public Command execute() {
        return null;
    }
}

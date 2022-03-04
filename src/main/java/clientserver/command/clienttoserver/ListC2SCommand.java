package clientserver.command.clienttoserver;

import command.Command;
import command.CommandType;
import command.ExecutableCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.Getter;
import lombok.Setter;

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
        LOGGER.debug("Executing ListInputCommand with content: {}", content);
        return null;
    }

}
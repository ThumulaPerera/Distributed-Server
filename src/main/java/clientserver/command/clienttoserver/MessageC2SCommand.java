package clientserver.command.clienttoserver;

import command.Command;
import command.CommandType;
import command.ExecutableCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageC2SCommand extends ExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageC2SCommand.class);

    private String content;

    public MessageC2SCommand() {
        super(CommandType.MESSAGE);
    }

    @Override
    public Command execute() {
        LOGGER.debug("Executing MessageInputCommand with content: {}", content);
        return this;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

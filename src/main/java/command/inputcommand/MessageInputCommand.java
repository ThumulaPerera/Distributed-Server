package command.inputcommand;

import command.CommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageInputCommand extends InputCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageInputCommand.class);

    private String content;

    public MessageInputCommand() {
        super(CommandType.MESSAGE);
    }

    @Override
    public void execute() {
        LOGGER.info("Executing MessageInputCommand with content: {}", content);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

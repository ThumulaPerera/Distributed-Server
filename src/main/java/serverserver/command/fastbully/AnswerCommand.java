package serverserver.command.fastbully;

import command.Command;
import command.CommandType;
import command.ExecutableCommand;
import command.S2SExecutableCommand;
import lombok.Getter;
import lombok.Setter;

public class AnswerCommand extends S2SExecutableCommand {
    @Getter @Setter private String from;
    public AnswerCommand() {
        super(CommandType.ANSWER);
    }

    @Override
    public Command execute() {
        return null;
    }
}

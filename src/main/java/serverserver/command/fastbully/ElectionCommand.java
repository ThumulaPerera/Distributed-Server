package serverserver.command.fastbully;

import command.Command;
import command.CommandType;
import command.ExecutableCommand;
import state.StateManagerImpl;

public class ElectionCommand extends ExecutableCommand {

    public ElectionCommand() {
        super(CommandType.ELECTION);
    }

    @Override
    public Command execute() {
        AnswerCommand answerCommand = new AnswerCommand();
        answerCommand.setFrom(StateManagerImpl.getInstance().getSelf().getId());
        return answerCommand;
    }
}

package serverserver.command.fastbully;

import command.Command;
import command.CommandType;
import command.ExecutableCommand;
import command.S2SExecutableCommand;
import state.StateManagerImpl;

public class ElectionCommand extends S2SExecutableCommand {

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

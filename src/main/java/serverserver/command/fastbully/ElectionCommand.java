package serverserver.command.fastbully;

import command.Command;
import command.CommandType;
import command.S2SExecutableCommand;
import state.RefinedStateManagerImpl;
import state.StateManager;

public class ElectionCommand extends S2SExecutableCommand {
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    public ElectionCommand() {
        super(CommandType.ELECTION);
    }

    @Override
    public Command execute() {
        AnswerCommand answerCommand = new AnswerCommand();
        answerCommand.setFrom(STATE_MANAGER.getSelf().getId());
        return answerCommand;
    }
}

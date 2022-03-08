package serverserver;

import config.Config;
import serverserver.command.fastbully.*;
import state.StateManagerImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class FastBully {
    private static final StateManagerImpl STATE_MANAGER = StateManagerImpl.getInstance();

    /**
     * Executes IamUp commands to peers
     * Accepts the existing leader or appoint self as leader and broadcast it to others
     */
    public static void executeStartup() {

        // TODO: setup T2 timeout for this
        STATE_MANAGER.getServers().forEach((serverId, server) -> {
            ViewCommand viewCommand = (ViewCommand) Sender.sendCommandToPeerAndReceive(
                    new IamUpCommand(), server);

            if (viewCommand != null) {
                viewCommand.execute();
            }
        });
        STATE_MANAGER.addAvailableServer(STATE_MANAGER.getSelf().getId());
        Set<String> availableServers = STATE_MANAGER.getAvailableServers();
        synchronized (availableServers) {
            String leaderId = Collections.max(STATE_MANAGER.getAvailableServers());
            STATE_MANAGER.setLeader(leaderId);
            if (leaderId.equals(STATE_MANAGER.getSelf().getId())) {

                // sending coordinator message to lower ranked processes
                CoordinatorCommand command = new CoordinatorCommand();
                command.setFrom(leaderId);
                for (String serverId : availableServers) {
                    if (serverId.compareTo(leaderId) < 0) {
                        Sender.sendCommandToPeer(command, STATE_MANAGER.getServers().get(serverId));
                    }
                }
            }
        }
    }

    public static void startElection() {
        // TODO: handle cases when ongoing election should be stopped
        STATE_MANAGER.setElectionAllowed(true);
        ArrayList<String> candidates = new ArrayList<>();
        String selfId = STATE_MANAGER.getSelf().getId();

        STATE_MANAGER.getServers().forEach((serverId, server) -> {
            if(!STATE_MANAGER.isElectionAllowed()) return;
            // send only to higher priority peers
            if (serverId.compareTo(selfId) > 0) {
                AnswerCommand answerCommand = (AnswerCommand) Sender.sendCommandToPeerAndReceive(
                        new ElectionCommand(), server);

                if (answerCommand != null) {
                    candidates.add(answerCommand.getFrom());
                }
            }
        });
        Collections.sort(candidates);
        boolean electionSuccess = false;
        while (candidates.size() > 0) {
            if(!STATE_MANAGER.isElectionAllowed()) return;
            String serverId = candidates.remove(candidates.size() - 1);
            CoordinatorCommand coordinatorCommand = (CoordinatorCommand) Sender.sendCommandToPeerAndReceive(
                    new NominationCommand(), STATE_MANAGER.getServers().get(serverId));

            if (coordinatorCommand != null) {
                coordinatorCommand.execute();
                electionSuccess = true;
                break;
            }
        }
        if (!electionSuccess) {
            try {
                Thread.sleep(Config.getFASTBULLY_T2());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(!STATE_MANAGER.isElectionAllowed()) return;
            FastBully.startElection();
        };

    }

    public static void sendCoordinatorCommandToOtherLowerPeers(CoordinatorCommand coordinatorCommand, String excludeServer){
        String selfId = STATE_MANAGER.getSelf().getId();
        STATE_MANAGER.getServers().forEach((serverId, server) -> {
            if (serverId.compareTo(selfId) < 0 && !serverId.equals(excludeServer)){
                Sender.sendCommandToPeer(coordinatorCommand, server);
            }
        });
    }
}

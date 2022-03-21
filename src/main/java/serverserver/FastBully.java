package serverserver;

import config.Config;
import serverserver.command.fastbully.*;
import state.RefinedStateManagerImpl;
import state.ServerModel;
import state.StateManager;

import java.util.ArrayList;
import java.util.Collections;

public class FastBully {
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    /**
     * Executes IamUp commands to peers
     * Accepts the existing leader or appoint self as leader and broadcast it to others
     */
    public static void executeStartup() {

        // TODO: setup T2 timeout for this
        STATE_MANAGER.getAllRemoteServers().forEach(server -> {
            // send only to other peers
            try {
                ViewCommand viewCommand = (ViewCommand) Sender.sendCommandToPeerAndReceive(
                        new IamUpCommand(), server);
                if (viewCommand != null) {
                    viewCommand.execute();
                }
            } catch (Exception ignored){
            }

        });

        // mark self as available
        STATE_MANAGER.addAvailableServerId(STATE_MANAGER.getSelf().getId());

        STATE_MANAGER.setLeaderOnStartup();
        if (STATE_MANAGER.isLeader()){
            // sending coordinator message to lower ranked processes
            CoordinatorCommand command = new CoordinatorCommand();
            command.setFrom(STATE_MANAGER.getSelf().getId());
            for (String serverId : STATE_MANAGER.getAvailableServerIds()) {
                if (serverId.compareTo(STATE_MANAGER.getSelf().getId()) < 0) {
                    Sender.sendCommandToPeer(command, STATE_MANAGER.getServer(serverId));
                }
            }
        }
    }

    public static void startElection() {
        // TODO: handle cases when ongoing election should be stopped
        STATE_MANAGER.setElectionAllowed(true);
        ArrayList<String> candidates = new ArrayList<>();
        String selfId = STATE_MANAGER.getSelf().getId();

        STATE_MANAGER.getAllRemoteServers().forEach(server -> {
            if (!STATE_MANAGER.isElectionAllowed()) return;
            // send only to higher priority peers
            if (server.getId().compareTo(selfId) > 0) {
                try {
                    AnswerCommand answerCommand = (AnswerCommand) Sender.sendCommandToPeerAndReceive(
                            new ElectionCommand(), server);

                    if (answerCommand != null) {
                        candidates.add(answerCommand.getFrom());
                    }
                } catch (Exception ignored){

                }
            }
        });

        if (candidates.isEmpty()){
            // if no higher process answers, then I am the leader
            STATE_MANAGER.setLeader(STATE_MANAGER.getSelf().getId());
            CoordinatorCommand command = new CoordinatorCommand();
            command.setFrom(STATE_MANAGER.getSelf().getId());
            for (ServerModel server : STATE_MANAGER.getAllRemoteServers()) {
                if (server.getId().compareTo(STATE_MANAGER.getSelf().getId()) < 0) {
                    Sender.sendCommandToPeer(command, server);
                }
            }
            return;
        }
        Collections.sort(candidates);
        boolean electionSuccess = false;
        while (candidates.size() > 0) {
            if (!STATE_MANAGER.isElectionAllowed()) return;
            String serverId = candidates.remove(candidates.size() - 1);
            try {
                CoordinatorCommand coordinatorCommand = (CoordinatorCommand) Sender.sendCommandToPeerAndReceive(
                        new NominationCommand(), STATE_MANAGER.getServer(serverId));

                if (coordinatorCommand != null) {
                    coordinatorCommand.execute();
                    electionSuccess = true;
                    break;
                }
            }catch (Exception ignored){

            }

        }

        if (!electionSuccess) {
            try {
                Thread.sleep(Config.getFASTBULLY_T2());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!STATE_MANAGER.isElectionAllowed()) return;
            FastBully.startElection();
        }
        ;

    }

    public static void sendCoordinatorCommandToOtherLowerPeers(CoordinatorCommand coordinatorCommand, String excludeServer) {
        String selfId = STATE_MANAGER.getSelf().getId();
        STATE_MANAGER.getAllRemoteServers().forEach(server -> {
            if (server.getId().compareTo(selfId) < 0 && !server.getId().equals(excludeServer)) {
                Sender.sendCommandToPeer(coordinatorCommand, server);
            }
        });
    }
}

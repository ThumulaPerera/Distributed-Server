package clientserver.command.clienttoserver;

import clientserver.Broadcaster;
import clientserver.command.servertoclient.RoomChangeS2CCommand;
import clientserver.command.servertoclient.ServerChangeS2CCommand;
import command.Command;
import command.CommandType;
import command.ClientAndSenderKnownExecutableCommand;
import command.ExecutableCommand;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.FastBully;
import serverserver.Sender;
import serverserver.command.followertoleader.ServerChangeNotificationF2LCommand;
import serverserver.command.leadertofollower.ServerChangeNotedL2FCommand;
import state.ChatRoomModel;
import state.RefinedStateManagerImpl;
import state.StateManager;

@Getter
@Setter
public class MoveJoinC2SCommand extends ClientAndSenderKnownExecutableCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(MoveJoinC2SCommand.class);
    private static final StateManager STATE_MANAGER = RefinedStateManagerImpl.getInstance();

    private String former;
    private String roomid;
    private String identity;

    public MoveJoinC2SCommand() {
        super(CommandType.MOVE_JOIN);
    }

    @Override
    public Command execute() {
        ChatRoomModel chatRoom = STATE_MANAGER.getLocalChatRoom(roomid);

        ServerChangeS2CCommand serverChangeMsg = new ServerChangeS2CCommand(
                true, STATE_MANAGER.getSelf().getId()
        );

        if (STATE_MANAGER.isLeader()){
            STATE_MANAGER.changeServerOfClient(identity, STATE_MANAGER.getSelf().getId());
        } else {
            try {
                ExecutableCommand response = Sender.sendCommandToLeaderAndReceive(
                        new ServerChangeNotificationF2LCommand(identity)
                );

                if (!(response instanceof ServerChangeNotedL2FCommand) || !((ServerChangeNotedL2FCommand) response).isNoted()) {
                    LOGGER.error("Notifying leader about server change failed");
                    getSender().send(new ServerChangeS2CCommand(false, STATE_MANAGER.getSelf().getId()));
                    return null;
                }
            }catch (Exception e){
                FastBully.startElection();
                LOGGER.error("Leader not available, calling election");
                getSender().send(new ServerChangeS2CCommand(false, STATE_MANAGER.getSelf().getId()));
                return null;
            }
        }

        if (chatRoom == null) {
            LOGGER.info("Requested Local Chatroom no longer available. Placing client in Main Hall instead.");
            STATE_MANAGER.addNewLocalClient(identity, getSender());
            RoomChangeS2CCommand broadcastMsg = new RoomChangeS2CCommand(
                    identity, former, STATE_MANAGER.getSelf().getMainHall()
            );
            getSender().send(serverChangeMsg);
            Broadcaster.broadcastToAllInMainHall(broadcastMsg);

        } else {
            LOGGER.info("Placing client in {}", roomid);
            STATE_MANAGER.addMoveJoinLocalClient(identity, getSender(), roomid);
            RoomChangeS2CCommand broadcastMsg = new RoomChangeS2CCommand(identity, former, roomid);
            getSender().send(serverChangeMsg);
            Broadcaster.broadcastToAll(roomid, broadcastMsg);
        }

        return null;
    }
}

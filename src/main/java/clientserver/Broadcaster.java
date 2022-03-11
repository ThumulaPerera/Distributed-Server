package clientserver;

import command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import state.LocalClientModel;
import state.StateManager;
import state.StateManagerImpl;

import java.util.List;

public class Broadcaster {
    private static final Logger LOGGER = LoggerFactory.getLogger(Broadcaster.class);
    private static final StateManager STATE_MANAGER = StateManagerImpl.getInstance();

    public static void broadcastToOthersInMainHall(Command command) {
        String mainHallId = STATE_MANAGER.getSelf().getMainHall();
        broadcastToOthers(mainHallId, command);
    }

    public static void broadcastToAllInMainHall(Command command) {
        String mainHallId = STATE_MANAGER.getSelf().getMainHall();
        broadcastToAll(mainHallId, command);
    }

    public static void broadcastToOthers(String chatRoomId, Command message) {
        broadcast(chatRoomId, message, false);
    }

    public static void broadcastToAll(String chatRoomId, Command message) {
        broadcast(chatRoomId, message, true);
    }

    private static void broadcast(String chatRoomId, Command message, boolean includeSelf) {
        List<LocalClientModel> clients = STATE_MANAGER.getLocalChatRoomClients(chatRoomId);
        synchronized (STATE_MANAGER.getLocalChatRoom(chatRoomId)) {
            clients.forEach(client -> {
                client.getSender().send(message);
            });
        }
    }
}



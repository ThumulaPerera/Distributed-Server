package state;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverserver.Sender;
import serverserver.command.leadertofollower.DeleteRoomL2FCommand;
import serverserver.command.leadertofollower.NewRoomL2FCommand;
import serverserver.command.leadertofollower.RemoveRoomL2FCommand;

import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class StateManagerImpl implements StateManager, StateInitializer {
    private static Logger LOGGER = LoggerFactory.getLogger(StateManagerImpl.class);
    private static StateManagerImpl instance = new StateManagerImpl();
    @Getter
    private final Map<String, ServerModel> servers;
    @Getter
    private final Set<String> availableServers = Collections.synchronizedSet(new HashSet<>());
    @Getter
    @Setter
    private volatile boolean electionAllowed = false;
    private volatile ServerModel leader;
    private ServerModel self;

    private StateManagerImpl() {
        servers = Collections.synchronizedMap(new HashMap<>());
    }

    public static StateManagerImpl getInstance() {
        return instance;
    }

    @Override
    public boolean isLeader() {
        boolean isLeader = self == leader;
//        LOGGER.debug("Is leader: {}", isLeader);
        return isLeader;
    }

    @Override
    public ServerModel getLeader() {
        return leader;
    }

    @Override
    public void setLeader(String leaderId) {
        leader = servers.get(leaderId);
//        leader.getHbdThread().start();
    }

    @Override
    public ServerModel getSelf() {
        return self;
    }

    @Override
    public void setSelf(String selfId) {
        self = servers.get(selfId);
    }


    @Override
    public boolean checkValidityAndAddLocalClient(String clientId, Socket socket) {
        synchronized (servers) {
            if (isIdentityTaken(clientId)) return false;
            addLocalClient(clientId, socket);
            return true;
        }
    }

    @Override
    public boolean checkValidityAndAddRoom(String roomId, String serverId, String clientId) {
        // Executed only by the leader
        LOGGER.debug("Checking global rooms for roomid: {}", roomId);
        synchronized (servers) {
            for (Map.Entry<String, ServerModel> server : servers.entrySet()) {
                if (server.getValue().containsChatRoom(roomId)) return false;
            }
            Sender sender = new Sender();
            for (Map.Entry<String, ServerModel> server : servers.entrySet()) {
                server.getValue().addChatRoom(new ChatRoomModel(roomId, new ClientModel(clientId), getServer(serverId)));

                // Send newroom to all the servers except the origin server
                if (!server.getValue().getId().equals(self.getId())) {
                    sender.sendCommandToPeer(new NewRoomL2FCommand(roomId, clientId, serverId), server.getValue());
                }
            }
            return true;
        }
    }

    @Override
    public boolean deleteRoom(String roomId) {
        // Executed only by the leader
        LOGGER.debug("Checking global rooms for roomid: {}", roomId);
        synchronized (servers) {
            Sender sender = new Sender();
            for (Map.Entry<String, ServerModel> server : servers.entrySet()) {
                if (server.getValue().containsChatRoom(roomId)) {
                    server.getValue().removeChatRoom(roomId);
                    if (!server.getValue().getId().equals(self.getId())) {
                        sender.sendCommandToPeer(new RemoveRoomL2FCommand(roomId), server.getValue());
                    }
                }
            }

            return true;
        }
    }


    public void addLocalClient(String clientId, Socket socket) {
        self.addClientToMainHall(new LocalClientModel(clientId, socket));
    }

    @Override
    public boolean checkValidityAndAddGlobalClient(String clientId, String serverId) {
        synchronized (servers) {
            if (isIdentityTaken(clientId)) return false;
            addGlobalClient(clientId, serverId);
            return true;
        }
    }

    @Override
    public List<LocalClientModel> getLocalChatRoomClients(String chatRoomId) {
        List<ClientModel> clientsMap = self.getChatRooms().get(chatRoomId).getClients();
        return clientsMap.stream().map(client -> (LocalClientModel) client).collect(Collectors.toList());
    }

    @Override
    public LocalClientModel getLocalClient(String clientId) {
        return (LocalClientModel) self.getClient(clientId);
    }

    private void addGlobalClient(String clientId, String serverId) {
        servers.get(serverId).addClientToMainHall(new ClientModel(clientId));
    }

    // should only be called from inside a block synchronized on servers
    private boolean isIdentityTaken(String clientId) {
        for (Map.Entry<String, ServerModel> server : servers.entrySet()) {
            for (Map.Entry<String, ChatRoomModel> room : server.getValue().getChatRooms().entrySet()) {
                ChatRoomModel chatRoom = room.getValue();
                if (chatRoom.containsClient(clientId)) return true;
            }
        }
        return false;
    }

    @Override
    public void addServer(String serverId, String serverAddress, int clientPort, int coordinationPort) {
        servers.put(serverId, new ServerModel(serverId, serverAddress, clientPort, coordinationPort));
    }

    public void addAvailableServer(String serverId) {
        this.availableServers.add(serverId);
    }

    public void removeAvailableServer(String serverId) {
        this.availableServers.remove(serverId);
    }

    @Override
    public ServerModel getServer(String serverId) {
        return servers.get(serverId);
    }


}

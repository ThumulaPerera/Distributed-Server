package state;

import clientserver.ClientSender;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class RefinedStateManagerImpl implements StateInitializer, StateManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(RefinedStateManagerImpl.class);
    private static final RefinedStateManagerImpl instance = new RefinedStateManagerImpl();
    private final Map<String, ServerModel> remoteServers = Collections.synchronizedMap(new HashMap<>());;
    private final Set<String> availableServers = Collections.synchronizedSet(new HashSet<>());
    private final Set<String> allClientIds = Collections.synchronizedSet(new HashSet<>());
    @Getter
    @Setter
    private volatile boolean electionAllowed = false;
    private volatile ServerModel leader;
    private LocalServerModel localServer;

    private RefinedStateManagerImpl() {}

    public static RefinedStateManagerImpl getInstance() {
        return instance;
    }


    @Override
    public void setLocalServer(String serverId, String serverAddress, int clientPort, int coordinationPort) {
        localServer = new LocalServerModel(serverId, serverAddress, clientPort, coordinationPort);
    }

    @Override
    public void addRemoteServer(String serverId, String serverAddress, int clientPort, int coordinationPort) {
        remoteServers.put(serverId, new ServerModel(serverId, serverAddress, clientPort, coordinationPort));
    }


    @Override
    public boolean isLeader() {
        return localServer.getId().equals(leader.getId());
    }

    @Override
    public ServerModel getLeader() {
        return leader;
    }

    @Override
    public void setLeader(String leaderId) {
        if (leaderId.equals(localServer.getId())) {
            leader = localServer;
        } else {
            leader = remoteServers.get(leaderId);
        }
    }

    @Override
    public ServerModel getSelf() {
        return localServer;
    }

    @Override
    public void addNewLocalClient(String clientId, ClientSender sender) {
        localServer.addClientToMainHall(new LocalClientModel(clientId, sender));
    }

    @Override
    public void addMoveJoinLocalClient(String clientId, ClientSender sender, String chatRoomId) {
        localServer.addClientToChatRoom(new LocalClientModel(clientId, sender), chatRoomId);
    }

    @Override
    public LocalClientModel removeLocalClientFromRoom(String clientId, String chatRoomId) {
        return localServer.removeClientFromChatRoom(clientId, chatRoomId);
    }

    @Override
    public boolean checkAvailabilityAndAddNewLocalClient(String clientId, ClientSender sender) {
        if (!checkAndGrabClientId(clientId)) return false;
        addNewLocalClient(clientId, sender);
        return true;
    }

    @Override
    public boolean checkAvailabilityAndAddGlobalClient(String clientId, String serverId) {
        return checkAndGrabClientId(clientId);
    }

    @Override
    public List<LocalClientModel> getLocalChatRoomClients(String chatRoomId) {
        return localServer.getClientsOfChatRoom(chatRoomId);
    }

    @Override
    public LocalClientModel getLocalClient(String clientId) {
        return localServer.getClient(clientId);
    }

    @Override
    public LocalChatRoomModel getLocalChatRoom(String chatRoomId) {
        return (LocalChatRoomModel) localServer.getChatRoom(chatRoomId);
    }

    @Override
    public boolean isIdLocallyAvailable(String clientId) {
        return localServer.getClient(clientId) == null;
    }

    @Override
    public ServerModel getServer(String serverId) {
        if (serverId.equals(localServer.getId())) {
            return localServer;
        }
        return remoteServers.get(serverId);
    }

    @Override
    public LocalChatRoomModel getRoomOfClient(String clientId) {
        return localServer.getRoomOfClient(clientId);
    }

    @Override
    public LocalChatRoomModel getRoomOwnedByClient(String clientId) {
        return localServer.getChatRoomByOwner(clientId);
    }

    @Override
    public ServerModel getServerIfGlobalChatRoomExists(String chatRoomId) {
        synchronized (remoteServers) {
            for (ServerModel server: remoteServers.values()) {
                if (server.getChatRoom(chatRoomId) != null) return server;
            }
        }
        return null;
    }

    @Override
    public void moveClientToChatRoom(String clientId, String fromRoomID, String toRoomId) {
        localServer.moveClientToChatRoom(clientId, fromRoomID, toRoomId);
    }

    @Override
    public List<ChatRoomModel> getAllChatRooms() {
        List<ChatRoomModel> chatRooms = new ArrayList<>();
        for (ServerModel server: remoteServers.values()) {
            chatRooms.addAll(server.getChatRooms());
        }
        chatRooms.addAll(localServer.getChatRooms());
        return chatRooms;
    }

    @Override
    public void addLocalRoom(String roomId, LocalClientModel owner) {
        localServer.addChatRoom(new LocalChatRoomModel(roomId, owner));
    }

    @Override
    public boolean checkValidityAndAddLocalRoom(String roomId, LocalClientModel owner) {
        synchronized (this) {
            if (!isRoomIdAvailable(roomId)) return false;
            addLocalRoom(roomId, owner);
        }
        return true;
    }

    @Override
    public boolean checkValidityAndAddRemoteRoom(String roomId, String managingServerId) {
        synchronized (this) {
            if (!isRoomIdAvailable(roomId)) return false;
            remoteServers.get(managingServerId).addChatRoom(new ChatRoomModel(roomId));
        }
        return true;
    }

    @Override
    public boolean deleteLocalRoom(String roomId) {
        synchronized (this) {
            if (!localServer.containsChatRoom(roomId)) return false;
            localServer.removeChatRoom(roomId);
            return true;
        }
    }

    @Override
    public void deleteGlobalRoom(String roomId) {
        synchronized (this) {
            for (ServerModel server: remoteServers.values()) {
                if (server.containsChatRoom(roomId)) {
                    server.removeChatRoom(roomId);
                };
            }
        }
    }

    @Override
    public List<ServerModel> getAllRemoteServers() {
        return new ArrayList<>(remoteServers.values());
    }

    @Override
    public void addRemoteChatRoom(String chatRoomId, String managingServerId) {
        remoteServers.get(managingServerId).addChatRoom(new ChatRoomModel(chatRoomId));
    }

    @Override
    public List<LocalChatRoomModel> getAllLocalChatRooms() {
        return localServer.getChatRooms().stream().map(chatRoomModel -> (LocalChatRoomModel)chatRoomModel).toList();
    }

    @Override
    public List<LocalClientModel> getAllLocalClients() {
        return localServer.getAllClients();
    }

    @Override
    public void addAvailableServerId(String serverId) {
        availableServers.add(serverId);
    }

    @Override
    public void setLeaderOnStartup() {
        synchronized (availableServers) {
            String leaderId = Collections.max(availableServers);
            setLeader(leaderId);
        }
    }

    @Override
    public List<String> getAvailableServerIds() {
        return new ArrayList<>(availableServers);
    }

    @Override
    public boolean isRoomIdAvailable(String roomId) {
        synchronized (remoteServers) {
            for (ServerModel server: remoteServers.values()) {
                if (server.containsChatRoom(roomId)) return false;
            }
        }
        return !localServer.containsChatRoom(roomId);
    }

    private boolean checkAndGrabClientId(String clientId) {
        synchronized (allClientIds) {
            if (allClientIds.contains(clientId)) return false;
            allClientIds.add(clientId);
        }
        return true;
    }







}

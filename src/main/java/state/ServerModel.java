package state;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.*;

@Getter
public class ServerModel {
    private final String id;
    private final String address;
    private final int clientsPort;
    private final int coordinationPort;
    @Getter(AccessLevel.NONE)
    private final Map<String, ChatRoomModel> chatRooms;

    public ServerModel(String serverId, String serverAddress, int clientsPort, int coordinationPort) {
        this.id = serverId;
        this.address = serverAddress;
        this.clientsPort = clientsPort;
        this.coordinationPort = coordinationPort;
        chatRooms = Collections.synchronizedMap(new HashMap<>());
        chatRooms.put("MainHall-" + id, new ChatRoomModel("MainHall-" + id, null));
    }

    public void addChatRoom(ChatRoomModel chatRoom) {
        chatRooms.put(chatRoom.getId(), chatRoom);
    }

    public void removeChatRoom(ChatRoomModel chatRoom) {
        chatRooms.remove(chatRoom.getId());
    }

    public ChatRoomModel getChatRoom(String chatRoomId) {
        return chatRooms.get(chatRoomId);
    }

    public boolean containsChatRoom(String chatRoomId) {
        return chatRooms.containsKey(chatRoomId);
    }

    public Map<String, ChatRoomModel> getChatRooms() {
        return chatRooms;
    }

    public void addClientToMainHall(ClientModel client) {
        chatRooms.get("MainHall-" + id).addClient(client);
    }
}

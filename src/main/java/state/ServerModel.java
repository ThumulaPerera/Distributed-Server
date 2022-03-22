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
    private final String mainHall;
    @Getter(AccessLevel.NONE)
    protected final Map<String, ChatRoomModel> chatRooms;

    public ServerModel(String serverId, String serverAddress, int clientsPort, int coordinationPort) {
        this.id = serverId;
        this.address = serverAddress;
        this.clientsPort = clientsPort;
        this.coordinationPort = coordinationPort;
        chatRooms = Collections.synchronizedMap(new HashMap<>());
        mainHall = "MainHall-" + id;
        chatRooms.put(mainHall, new ChatRoomModel(mainHall));
    }

    protected void addChatRoom(ChatRoomModel chatRoom) {
        chatRooms.put(chatRoom.getId(), chatRoom);
    }

    protected ChatRoomModel removeChatRoom(String chatRoomId) {
        return chatRooms.remove(chatRoomId);
    }

    protected ChatRoomModel getChatRoom(String chatRoomId) {
        return chatRooms.get(chatRoomId);
    }

    protected boolean containsChatRoom(String chatRoomId) {
        return chatRooms.containsKey(chatRoomId);
    }

    protected List<ChatRoomModel> getChatRooms() {
        return chatRooms.values().stream().toList();
    }

    protected void removeAllChatRoomsExceptMainHall(){
        synchronized (chatRooms) {
            chatRooms.keySet().removeIf(chatRoomId -> !chatRoomId.equals(mainHall));
        }
    }

}

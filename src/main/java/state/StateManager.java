package state;

public interface StateManager {
    boolean isLeader();

    ServerModel getLeader();

    void setLeader(String leaderId);

    ServerModel getSelf();

    void setSelf(String selfId);

    boolean checkValidityAndAddClient(String clientId, String serverId);

    boolean checkValidityAndAddRoom(String roomId, String serverId, String clientId);

    ServerModel getServer(String serverId);
//    void addLocalChatRoom(String chatRoomId);
//    void removeLocalChatRoom(String chatRoomId);
//    Map<String, ChatRoomModel> getLocalChatRooms();
//    void addRemoteChatRoom(String chatRoomId, String serverId);
//    void removeRemoteChatRoom(String chatRoomId, String serverId);
//    Map<String, Map<String, ChatRoomModel>> getRemoteChatRooms();
//    Map<String, Map<String, ChatRoomModel>> getAllChatRooms();
//    void addLocalClient(String clientId, String chatRoomId);
//    void removeLocalClient(String clientId, String chatRoomId);
//    Map<String, Map<String, ClientModel>> getLocalClients();

}

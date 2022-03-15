package state;

// to be used for initializing the state
public interface StateInitializer {
    void setLocalServer(String serverId, String serverAddress, int clientPort, int coordinationPort);
    void addRemoteServer(String serverId, String serverAddress, int clientPort, int coordinationPort);
}

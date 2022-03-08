package state;

// to be used for initializing the state
public interface StateInitializer {
    void addServer(String serverId, String serverAddress, int clientPort, int coordinationPort);
}

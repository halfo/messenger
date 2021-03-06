package io.github.halfo;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class SocketList {
    private static final SocketList instance = new SocketList();
    private Map<String, Socket> mappedSockets;

    protected SocketList() {
        mappedSockets = new HashMap();
    }
    
    public SocketList getInstance() {
        return instance;
    }

    public synchronized void storeSocket(String username, Socket socket) {
        mappedSockets.put(username, socket);
    }

    public Socket getSocket(String username) {
        return mappedSockets.get(username);
    }

    public synchronized void removeSocket(String username) {
        mappedSockets.remove(username);
    }

    public boolean isUserOnline(String username){
        return mappedSockets.containsKey(username);
    }

    public Set<String> getAvailableUsers() {
        return mappedSockets.keySet();
    }
}

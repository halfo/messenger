package io.github.halfo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
    private ServerSocket serverSocket;
    private Validator validator;
    private SocketList socketList;

    Server(int port) throws IOException, FileNotFoundException {
        serverSocket = new ServerSocket(port);
        validator    = new Validator().getInstance();
        socketList   = new SocketList().getInstance();
        run();
    }

    // format = "username:password"
    private String getUsername(String userData) {
        return userData.substring(0, userData.indexOf(":"));
    }

    // format = "username:password"
    private String getPassword(String userData) {
        return userData.substring(userData.indexOf(":") + 1, userData.length());
    }

    private void run() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            BufferedReader userDataReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream responseToUser = new DataOutputStream(socket.getOutputStream());

            String userData = userDataReader.readLine();
            String username = getUsername(userData);
            String password = getPassword(userData);

            if (validator.match(username, password) &&
                    !socketList.isUserOnline(username)) {
                responseToUser.writeBytes("success\n");
                assignSession(username, socket);
            } else {
                responseToUser.writeBytes("failed\n");
            }
        }
    }

    private void assignSession(String username, Socket socket) {
        socketList.storeSocket(username, socket);

        Session session = new Session(username);
        session.addKiller(new SessionKiller());
        session.start();
    }

    class SessionKiller implements Killer {
        @Override
        public synchronized void kill(Session session) {
            String username = session.getSender();
            socketList.removeSocket(username);

            session.interrupt();
        }
    }
}

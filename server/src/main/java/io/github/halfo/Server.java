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
    private String getUsername(String message) {
        return message.substring(0, message.indexOf(":"));
    }

    // format = "username:password"
    private String getPassword(String message){
        return message.substring(message.indexOf(":") + 1, message.length());
    }

    private void run() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream userNotifier = new DataOutputStream(socket.getOutputStream());

            String encodedMessage = reader.readLine();
            String username = getUsername(encodedMessage);
            String password = getPassword(encodedMessage);

            if (validator.match(username, password) &&
                    !socketList.isUserOnline(username)) {
                userNotifier.writeBytes("success\n");
                assignSession(username, socket);
            } else {
                userNotifier.writeBytes("failed\n");
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
        public void kill(Session session) {
            String username = session.getSender();
            socketList.removeSocket(username);

            session.interrupt();
        }
    }
}
package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class Client {
    private String username;
    private static BufferedReader reader;
    private static DataOutputStream writer;

    public void connectToServer(String server, int port) throws IOException {
        Socket socket = new Socket(server, port);

        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new DataOutputStream(socket.getOutputStream());
    }

    public boolean attemptToLogin(String username, String password) throws IOException {
        sendMessage(encode(username, password));
        String verdict = readMessage();

        if (verdict.equals("success")) {
            this.username = username;
            return true;
        }

        return false;
    }

    public String getUsername() {
        return username;
    }

    public String readMessage() throws IOException {
        return reader.readLine();
    }

    public String encode(String receiver, String message) {
        return receiver + ":" + message;
    }

    public String justifyReceiver(String receiver) {
        if (receiver == null || receiver.equals("")) {
            receiver = "ALL";
        }

        return receiver;
    }

    public boolean sendMessage(String encodedMessage) {
        try {
            writer.writeBytes(encodedMessage + "\n");
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}

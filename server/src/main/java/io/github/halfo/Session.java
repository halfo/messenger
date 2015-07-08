package io.github.halfo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Set;

class Session extends Thread {
    private String sender;
    private BufferedReader userMessageReader;
    private SocketList socketList;
    private Killer killer;

    Session(String sender) {
        this.sender = sender;
        socketList = new SocketList().getInstance();

        try {
            Socket socket = socketList.getSocket(sender);
            userMessageReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            killThis();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String encodedMessage = userMessageReader.readLine();
                String receiver = getReceiver(encodedMessage);
                String message  = getOriginalMessage(encodedMessage);

                sendMessage(receiver, message);
            } catch (Exception e) {
                killThis();
            }
        }
    }

    private void sendMessage(Socket receiverSocket, String message) {
        try {
            DataOutputStream messageWriter = new DataOutputStream(receiverSocket.getOutputStream());
            messageWriter.writeBytes(message + "\n");
        } catch (IOException e) {
            killThis();
        }
    }

    private void sendMessage(String receiver, String message) {
        if (receiver.equals(sender)) return;

        if (receiver.equals("ALL")) {
            Set<String> availableUsers = socketList.getAvailableUsers();
            message = encode("ALL", message);
            for (String user : availableUsers) {
                if (user.equals(sender)) continue;
                sendMessage(socketList.getSocket(user), message);
            }
        } else if (socketList.isUserOnline(receiver)) {
            message = encode("You", message);
            sendMessage(socketList.getSocket(receiver), message);
        } else {
            Set<String> availableUsers = socketList.getAvailableUsers();
            sendMessage(socketList.getSocket(sender), genErrorMessage("Something went wrong..."));
        }
    }

    private String genErrorMessage(String errorMessage) {
        return "Server >> you :: " + errorMessage;
    }

    // unicast   format = "sender >> you :: message"
    // broadcast format = "sender >> ALL :: message"
    private String encode(String receiver, String message) {
        return sender + " >> " + receiver + " :: " + message;
    }

    // format = "receiver:message"
    private String getReceiver(String encodedMessage) {
        return encodedMessage.substring(0, encodedMessage.indexOf(":"));
    }

    // format = "receiver:message"
    private String getOriginalMessage(String encodedMessage) {
        return encodedMessage.substring(encodedMessage.indexOf(":") + 1, encodedMessage.length());
    }

    public String getSender() {
        return sender;
    }

    public void addKiller(Killer killer) {
        this.killer = killer;
    }

    private void killThis() {
        killer.kill(this);
    }
}

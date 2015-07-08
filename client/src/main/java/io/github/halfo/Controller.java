package io.github.halfo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

class Controller {
    private Client client;
    private LoginForm loginForm;
    private ChatFrame chatFrame;

    Controller() {
        client = new Client();
        loginForm = new LoginForm();
        chatFrame = new ChatFrame();

        this.loginForm.addLoginButtonListener(new LoginButtonListener());
        this.chatFrame.addSendButtonListener(new SendButtonListener());

        loginForm.setVisible(true);
    }

    private void listenToServer() {
        Runnable runnableTask = () -> {
            try {
                while(true) {
                    String message = client.readMessage();
                    if (message == null)
                        continue;

                    chatFrame.addMessageToMessageBoard(message);
                }
            } catch (IOException ex) {
                chatFrame.showErrorMessage();
            }
        };

        new Thread(runnableTask).start();
    }

    class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                client.connectToServer("localhost", 8000);
                boolean isSuccess = client.attemptToLogin(loginForm.getUsername(), loginForm.getPassword());

                if (isSuccess) {
                    loginForm.setVisible(false);
                    loginForm.dispose();

                    chatFrame.setVisible(true);
                    chatFrame.setUsername(client.getUsername());
                    listenToServer();
                } else {
                    loginForm.showErrorMessage();
                }
            } catch (IOException ex) {
                loginForm.showErrorMessage();
            }
        }

    }

    class SendButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String receiver = chatFrame.getReceiver();    
            String message  = chatFrame.getMessage();

            receiver = client.justifyReceiver(receiver);
            if (client.getUsername().equals(receiver)) return;
            boolean success = client.sendMessage(client.encode(receiver, message));
            if (success) {
                chatFrame.addMessageToMessageBoard(chatFrame.encode(receiver, message));
                chatFrame.clearFields();
            } else {
                chatFrame.showErrorMessage();
            }
        }        
    }
}

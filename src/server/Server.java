package server;

import javax.swing.*;
import java.awt.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.MessageFormat;

import static server.ServerConsts.*;

public class Server extends JFrame {

    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;

    public Server() {
        super(SERVER_NAME);
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
                event -> {
                    sendMessage(event.getActionCommand());
                    userText.setText(DEFAULT_USER_TEXT);
                }
        );
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow));
        setSize(DEFAULT_WINDOW_WIDTH,
                DEFAULT_WINDOW_HEIGHT);
        setVisible(true);
    }

    //set up and run the server
    public void run() {
        try {
            server = new ServerSocket(PORT, 100);
            while (true) {
                try {
                    waitForConnection();
                    setupStreams();
                    whileChatting();
                } catch (EOFException e) {
                    showMessage(CONNECTION_TERMINATION_MESSAGE);
                } finally {
                    closeStuff();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //listen for connection, then display connection information
    private void waitForConnection() throws IOException {
        showMessage(WAITING_FOR_CONNECTION);
        connection = server.accept();
        String message = MessageFormat.format(CONNECTED_TO_CLIENT,
                connection.getInetAddress().getHostName());
        showMessage(message);
    }

    private void setupStreams() throws IOException {
        input = new ObjectInputStream(connection.getInputStream());
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
    }

    private void whileChatting() {
        String message = "You are now connected!";
        sendMessage(message);
        ableToType(true);

        do {
            try {
                message = (String) input.readObject();
                showMessage("\n" + message);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } while (!message.equals(END_CONNECTION_KEYWORD));
    }

    //close streams and sockets after done chatting
    private void closeStuff() {
        showMessage("\nClosing connections...");
        ableToType(false);
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //send message to client
    private void sendMessage(String message) {
        try {
            output.writeObject("SERVER - " + message);
            output.flush();
            showMessage("\nSERVER - " + message);
        } catch (IOException e) {
            chatWindow.append(SEND_MESSAGE_FAIL);
        }
    }

    //updates chat window
    private void showMessage(final String message) {
        SwingUtilities.invokeLater(() -> chatWindow.append(message));
    }

    //let the user type stuff into their box
    private void ableToType(final boolean isAble) {
        SwingUtilities.invokeLater(() -> userText.setEditable(isAble));
    }


}

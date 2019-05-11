package client;

import javax.swing.*;
import java.awt.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.MessageFormat;

import static client.ClientConsts.*;

public class Client extends JFrame {
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String message;
    private String serverIP;
    private Socket connection;

    public Client(String hostIP) {
        super(CLIENT_NAME);
        serverIP = hostIP;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(event -> {
            sendMessage(event.getActionCommand());
            userText.setText(DEFAULT_USER_TEXT);
        });
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(DEFAULT_WINDOW_WIDTH,
                DEFAULT_WINDOW_HEIGHT);
        setVisible(true);
    }

    //connect to server
    public void startRunning() {
        try {
            connectToServer();
            setupStreams();
            whileChatting();
        } catch (EOFException eof) {
            showMessage(CONNECTION_TERMINATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStuff();
        }
    }

    private void connectToServer() throws IOException {
        showMessage("Attempting connection...\n");
        final int port = 6789;
        connection = new Socket(InetAddress.getByName(serverIP), port);
        String message = MessageFormat.format(CONNECTED_TO_SERVER,
                connection.getInetAddress().getHostName());
    }

    //setup streams to send and receive messages
    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
    }

    //while chatting with server
    private void whileChatting() {
        do {
            try {
                ableToType(true);
                message = (String) input.readObject();
                showMessage("\n" + message);
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        } while (!message.equals(END_CONNECTION_KEYWORD));
    }

    //close streams and sockets after done chatting
    private void closeStuff() {
        final String CLOSING_MESSAGE = "Closing streams and sockets...\n";
        showMessage(CLOSING_MESSAGE);
        ableToType(false);
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //send messages to server
    private void sendMessage(String message) {
        try {
            output.writeObject("CLIENT - " + message);
            output.flush();
            showMessage("\nCLIENT - " + message);
        } catch (IOException e) {
            chatWindow.append(SEND_MESSAGE_FAIL);
        }
    }

    //change/update chat window
    private void showMessage(final String message) {
        SwingUtilities.invokeLater(() -> chatWindow.append(message));
    }

    //give user permission to type
    private void ableToType(boolean isAble) {
        SwingUtilities.invokeLater(() -> userText.setEditable(isAble));
    }

}

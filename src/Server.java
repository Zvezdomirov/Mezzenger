import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends JFrame {
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;

    public Server() {
        super("Mezzenger");
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent event) {
                        sendMessage(event.getActionCommand());
                        userText.setText("");
                    }
                }
        );
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow));
        setSize(300, 150);
        setVisible(true);

        //set up and run the server
    }

    public void run() {
        try {
            server = new ServerSocket(6789, 100);
            while (true) {
                try {
                    waitForConnection();
                    setupStreams();
                    whileChatting();
                } catch (EOFException e) {
                    showMessage("\n Server ended the connection!");
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
        showMessage("Waiting for someone to connect... ");
        connection = server.accept();
        showMessage("Now connected to " + connection.getInetAddress().getHostName());
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
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } while (!message.equals("CLIENT - END"));
    }

    //close streams and sockets after you are done chatting
    private void closeStuff() {
        showMessage("\nClosing connections...\n");
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
        } catch (IOException e) {
            chatWindow.append("\n ERROR: Message cannot be sent.");
        }
    }

    //updates chat window
    private void showMessage(final String text) {
        SwingUtilities.invokeLater(() -> chatWindow.append(text));
    }

    //let the user type stuff into their box
    private void ableToType(final boolean tof) {
        SwingUtilities.invokeLater(() -> userText.setEditable(tof));
    }


}

import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends JFrame {
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String message;
    private String serverIP;
    private Socket connection;

    public Client(String host) {
        super("CLIENT");
        serverIP = host;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(event -> {
            sendData(event.getActionCommand());
            userText.setText("");
        });
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(300, 150);
        setVisible(true);
    }

}

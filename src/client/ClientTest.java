package client;

import javax.swing.*;

public class ClientTest {
    private final static String hostIP = "127.0.0.1";

    public static void main(String[] args) {
        Client client;
        client = new Client(hostIP);
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.startRunning();
    }
}

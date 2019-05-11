package client;

public class ClientConsts {
    private ClientConsts() {
        /*this prevents even the native class from
         * calling this constructor as well*/
        throw new AssertionError();
    }

    static final String CLIENT_NAME = "CLIENT";
    static final String DEFAULT_USER_TEXT = "";
    static final int DEFAULT_WINDOW_WIDTH = 300;
    static final int DEFAULT_WINDOW_HEIGHT = 150;
    static final String CONNECTION_TERMINATION_MESSAGE = "\nClient terminated the connection.";
    static final String CONNECTED_TO_SERVER = "\nNow connected to: {0}";
    static final String END_CONNECTION_KEYWORD = "SERVER - END";
    static final String SEND_MESSAGE_FAIL = "\nError: Failed to send the message.";
}

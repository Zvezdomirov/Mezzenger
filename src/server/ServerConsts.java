package server;

public class ServerConsts {
    private ServerConsts() {
        /*this prevents even the native class from
        * calling this constructor as well*/
        throw new AssertionError();
    }
    static final String SERVER_NAME = "Mezzenger";
    static final int PORT = 6789;
    static final String DEFAULT_USER_TEXT = "";
    static final int DEFAULT_WINDOW_WIDTH = 300;
    static final int DEFAULT_WINDOW_HEIGHT = 150;
    static final String CONNECTION_TERMINATION_MESSAGE = "\nServer terminated the connection!";
    static final String WAITING_FOR_CONNECTION = "\nWaiting for someone to connect...";
    static final String CONNECTED_TO_CLIENT = "\nNow connected to: {0}";
    static final String END_CONNECTION_KEYWORD = "CLIENT - END";
    static final String SEND_MESSAGE_FAIL = "\nError: Failed to send the message.";

}

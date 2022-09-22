package distributed.broker;

import java.net.Socket;

public record SubscriberInfo(Socket socket,String nodeId) {
}

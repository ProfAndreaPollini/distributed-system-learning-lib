package distributed.broker;

import distributed.core.Host;
import distributed.core.TCPServiceServer;
import distributed.core.ThreadPerTaskExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MessageBrokerServer extends TCPServiceServer {

    private final HashMap<String, List<SubscriberInfo>> subscriptions = new HashMap<>();
    public MessageBrokerServer(int port) {
        super(port);
    }



    protected void handle(Socket socket, InputStream fromClient, OutputStream toClient) throws IOException {

        byte[] bytes = new byte[1024];
        int len;
        len = fromClient.read(bytes);
        var inString = new String(bytes,0,len, StandardCharsets.UTF_8);
        System.out.println(String.format("MESSAGEBROKER>>> %s",inString));
        for(var cmd: inString.strip().split("\r\n")) {
            processCommand(socket, cmd);
        }
    }
    
    private void processCommand(Socket socket, String inString) throws IOException {
        var paramsString = inString.substring(1, inString.length());
        var params = paramsString.split(":");
        switch(inString.charAt(0)) {
            case 'P':
                handlePublish(params[0],params[1]);
                break;
            case 'S':
                handleSubscribe(socket,params[0],params[1]);
                break;
        }
    }
    
    private void handleSubscribe(Socket socket,String topic, String nodeId) {
        //var hosts = subscriptions.getOrDefault(topic,new ArrayList<Host>());
        List<SubscriberInfo> subscribers = new ArrayList<SubscriberInfo>();
        if (subscriptions.containsKey(topic)) {
            subscribers = subscriptions.get(topic);
        }
        subscribers.add(new SubscriberInfo(socket,nodeId));
        subscriptions.put(topic,subscribers);
        var subsNames = subscriptions.keySet().stream().map(
                topicL -> {
                    return String.format("%s [%d]",topicL,subscriptions.get(topicL).size());
                }
        ).collect(Collectors.joining(", "));
        System.out.println(String.format("subscriptions [%d] =>{%s}",subscriptions.keySet().size(),subsNames));
    }

    private void handlePublish(String topic, String payload) throws IOException {
        var topicSubscribers = subscriptions.getOrDefault(topic,new ArrayList<>());
        for(var subscriber: topicSubscribers) {
            var out = subscriber.socket().getOutputStream();
            out.write(String.format("%s:%s",topic,payload).getBytes());
            out.flush();
        }
    }
}

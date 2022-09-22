package distributed.broker;

import distributed.core.TCPServiceClient;
import distributed.core.ThreadPerTaskExecutor;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MessageBrokerClient extends TCPServiceClient implements Runnable{
    @Override
    public void run() {
        DataInputStream in  = null;
        try {
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while(true) {
            try {
                var data = readFromInputStream(in);
                if (data != null) {
                  var params = data.split(":");
                  var topic = params[0];
                  var payload = params[1];
                  System.out.println("MessageBrokerClient received " + data);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    private final Socket socket;
//    private final DataInputStream in;
//    private PrintStream out;
   private final boolean isRunning = true;
    
    public MessageBrokerClient(String serverAddress, int serverPort) {
        
        super(serverAddress, serverPort);
        socket = connectToServer();
    
    }

    public void subscribe(String topic, String myname) {
        
    
        try {
//            var in = new DataInputStream(socket.getInputStream());
            var out = new PrintWriter(socket.getOutputStream(), true);//PrintStream(socket.getOutputStream());
            out.println(String.format("S%s:%s",topic,myname));
            //out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        //close();
    }
    
    private void close() throws IOException {
//        in.close();
//        out.close();
        socket.close();
    }
    
    public void publish(String topic, String payload) {
    
        try {
            var out = new PrintWriter(socket.getOutputStream(),true);
            out.println(String.format("P%s:%s",topic,payload));
           // out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
       
    
    }
}

package distributed.servicediscovery;

import distributed.core.Host;
import distributed.core.TCPServiceClient;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServiceDiscoveryClient extends TCPServiceClient {
	
	public ServiceDiscoveryClient(String serverAddress, int serverPort) {
		super(serverAddress, serverPort);
	}
	
	public void register(ServiceInfo serviceInfo){
		try {
			Socket socket = connectToServer();
			var in = new DataInputStream(socket.getInputStream());
			var out = new PrintStream(socket.getOutputStream());
			out.println(String.format("REGISTER:%s:%s:%d",serviceInfo.service,serviceInfo.addess,serviceInfo.port));
			out.flush();
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Host> lookup(String serviceName){
		
		try {
			var ret = new ArrayList<Host>();
			Socket socket = connectToServer();
			var in = new DataInputStream(socket.getInputStream());
			var out = new PrintStream(socket.getOutputStream());
			out.println(String.format("LOOKUP:%s",serviceName));
			out.flush();
			//var response = in.readUTF();
			var response = readFromInputStream(in);
			System.out.println("<<<" + response);
			in.close();
			out.close();
			socket.close();
			return ret;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	
}

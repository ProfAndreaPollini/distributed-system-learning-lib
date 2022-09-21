package distributed.heartbeat;

import distributed.core.UDPServer;

import java.net.InetAddress;

public class HeartbeatServer extends UDPServer {
	public HeartbeatServer(int port) {
		super(port);
	}
	
	@Override
	protected void handleMessage(String message, InetAddress address, int port) {
		System.out.println("HEARTBEAT -> " + message);
	}
}

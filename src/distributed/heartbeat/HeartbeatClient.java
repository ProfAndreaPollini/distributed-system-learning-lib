package distributed.heartbeat;

import distributed.core.UDPClient;

import java.net.DatagramPacket;
import java.util.Timer;
import java.util.TimerTask;

public class HeartbeatClient extends UDPClient implements Runnable  {
	
	boolean isRunning = true;
	
	private final String serverAddress;
	private final int serverPort;
	
	public HeartbeatClient( String serverAddress, int serverPort) {
		super(serverAddress,serverPort);
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
	}
	
	@Override
	public void run() {
//
		while (isRunning) {
			try {
				Thread.sleep(1000);
				send("test");
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			
		}
		
	}
}

package distributed.core;

import java.io.IOException;
import java.net.*;

public abstract class UDPServer extends Thread {
	
	private final DatagramSocket socket;
	private final byte[] buf = new byte[256];
	
	protected UDPServer(int port) {
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void run() {
		var running = true;
		try {
		while (running) {
			DatagramPacket packet
							= new DatagramPacket(buf, buf.length);
		
				socket.receive(packet);
		
			
			InetAddress address = packet.getAddress();
			int port = packet.getPort();
			packet = new DatagramPacket(buf, buf.length, address, port);
			String received
							= new String(packet.getData(), 0, packet.getLength());
			handleMessage(received,address,port);
//			if (received.equals("end")) {
//				running = false;
//				continue;
//			}
//			socket.send(packet);
		}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		socket.close();
	}
	
	protected abstract void handleMessage(String message, InetAddress address, int port);
}

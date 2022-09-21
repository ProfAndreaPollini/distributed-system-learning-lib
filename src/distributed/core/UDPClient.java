package distributed.core;

import java.io.IOException;
import java.net.*;

public class UDPClient {
	private DatagramSocket socket;
	private InetAddress address;
	private int port;
	
	private byte[] buf;
	
	public UDPClient(String hostname,int port) {
		
		try {
			socket = new DatagramSocket();
			address = InetAddress.getByName(hostname);
			this.port = port;
//			socket.connect(new InetSocketAddress(hostname,port));
//			socket.bind(new InetSocketAddress(hostname,port));
		} catch (SocketException e) {
			throw new RuntimeException(e);
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
		
		
	}
	
	public void send(String msg) {
		buf = msg.getBytes();
		DatagramPacket packet
						= new DatagramPacket(buf, buf.length, address, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
//		packet = new DatagramPacket(buf, buf.length);
//		socket.receive(packet);
//		String received = new String(
//						packet.getData(), 0, packet.getLength());
//		return received;
	}
	
	public DatagramPacket receive() {
		
		try {
			byte[] buf = new byte[1024];
			var packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
//			String received = new String(
//							packet.getData(), 0, packet.getLength());
//			return received;
			return packet;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	
	}
	
	public void close() {
		socket.close();
	}
}

package distributed.core;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPServiceClient  {
	protected final String serverAddress;
	protected final int serverPort;
	
	public TCPServiceClient(String serverAddress, int serverPort) {
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
	}
	
	protected Socket connectToServer() {
		try {
			var socket = new Socket(serverAddress, serverPort);
			socket.setTcpNoDelay(true);
			return socket;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected String readFromInputStream(DataInputStream in) throws IOException {
		byte[] bytes = new byte[1024];
		int len = in.read(bytes);
		if (len <0) return null;
		return new String(bytes, 0, len, StandardCharsets.UTF_8);
	}
}

package distributed.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public abstract class TCPServiceServer extends Thread {
	protected final int port;
	protected boolean stopServer;
	
	public TCPServiceServer(int port) {
		this.port = port;
		stopServer = false;
	}
	
	protected static String readFromClient(InputStream fromClient, int maxBytes) throws IOException {
		byte[] bytes = new byte[maxBytes];
		int len;
		len = fromClient.read(bytes);
		var inString = new String(bytes,0,len, StandardCharsets.UTF_8);
		return inString;
	}
	
	@Override
	public void run() {
		try {
			var serverSocket = new ServerSocket(port);
			System.out.println("Waiting for clients to connect...");
			var executor = new ThreadPerTaskExecutor();
			
			while (!stopServer) {
				
				Socket socket = serverSocket.accept();
				System.out.println("Client accepted");
				Runnable task = () -> {  // lambda function
					try {
						var in  = socket.getInputStream();
						var out = socket.getOutputStream();
						handle(socket,in, out);
//						in.close();
//						out.close();
//						socket.close();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				};
				executor.execute(task);
				
			}
			serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	abstract protected void handle(Socket socket, InputStream in, OutputStream out) throws IOException;
	
}

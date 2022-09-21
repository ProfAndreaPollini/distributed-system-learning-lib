package distributed.servicediscovery;

import java.util.concurrent.Executor;

public class TCPServiceServer extends Thread {
	protected final int port;
	protected boolean stopServer;
	
	public TCPServiceServer(int port) {
		this.port = port;
		stopServer = false;
	}
	
	static class ThreadPerTaskExecutor implements Executor {
		public void execute(Runnable r) {
			try {
				var t = new Thread(r);
				t.start();
				t.join();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
}

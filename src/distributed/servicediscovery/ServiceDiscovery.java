package distributed.servicediscovery;

import distributed.gateway.APIGateway;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;

public class ServiceDiscovery extends Thread{
	private final int port;
	private final ServiceDiscoveryHandler handler;
	private boolean stopServer;
	private HashMap<String, List<ServiceInfo>> services = new HashMap<>();
	
	public ServiceDiscovery(int port, ServiceDiscoveryHandler handler) {
		this.port = port;
		this.handler = handler;
		stopServer = false;
		this.handler.setServiceDiscovery(this);
	}
	
	
	@Override
	public void run() {
		try {
			var serverSocket = new ServerSocket(port);
			System.out.println("Waiting for clients to connect...");
			var executor = new ThreadPerTaskExecutor();
			while (!stopServer) {

				Socket finalClientSocket = serverSocket.accept();
				Runnable task = () -> {
					try {
						var in  = finalClientSocket.getInputStream();
						var out = finalClientSocket.getOutputStream();
						handler.handle(in, out);
						in.close();
						out.close();
						finalClientSocket.close();
					} catch (ClassNotFoundException e) {
						throw new RuntimeException(e);
					} catch (InterruptedException | IOException e) {
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
	
	public void registerService(ServiceInfo serviceInfo) {
		
		if(!services.containsKey(serviceInfo.service)) {
			services.put(serviceInfo.service, new ArrayList<>());
		}
		var hosts = services.get(serviceInfo.service);
		hosts.add(serviceInfo);
		services.put(serviceInfo.service,hosts);
	}
	
	public List<ServiceInfo> lookup(String service) {
		return services.get(service);
	}
	
	static class ThreadPerTaskExecutor implements Executor {
		public void execute(Runnable r) {
			try {
				var t= new Thread(r);
				t.start();
				t.join();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

}

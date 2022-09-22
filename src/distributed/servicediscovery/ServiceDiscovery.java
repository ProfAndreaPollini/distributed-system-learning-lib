package distributed.servicediscovery;

import distributed.core.TCPServiceServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class ServiceDiscovery extends TCPServiceServer {
	private final ServiceDiscoveryHandler handler;
	private HashMap<String, List<ServiceInfo>> services = new HashMap<>();
	
	public ServiceDiscovery(int port, ServiceDiscoveryHandler handler) {
		super(port);
		this.handler = handler;
		this.handler.setServiceDiscovery(this);
	}
	
	
//	@Override
//	public void run() {
//		try {
//			var serverSocket = new ServerSocket(port);
//			System.out.println("Waiting for clients to connect...");
//			var executor = new ThreadPerTaskExecutor();
//			while (!stopServer) {
//
//				Socket finalClientSocket = serverSocket.accept();
//				System.out.println("Client accepted");
//				Runnable task = () -> {
//					try {
//						var in  = finalClientSocket.getInputStream();
//						var out = finalClientSocket.getOutputStream();
//						handler.handle(in, out);
//						in.close();
//						out.close();
//						finalClientSocket.close();
//					} catch (ClassNotFoundException e) {
//						throw new RuntimeException(e);
//					} catch (InterruptedException | IOException e) {
//						throw new RuntimeException(e);
//					}
//				};
//				executor.execute(task);
//
//			}
//			serverSocket.close();
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//
//	}
	
	public void registerService(ServiceInfo serviceInfo) {
		
		if(!services.containsKey(serviceInfo.service)) {
			services.put(serviceInfo.service, new ArrayList<>());
		}
		var hosts = services.get(serviceInfo.service);
		hosts.add(serviceInfo);
		services.put(serviceInfo.service,hosts);
		var servicesNames = services.keySet().stream().map(
						service -> {
							return String.format("%s [%d]",service,services.get(service).size());
						}
		).collect(Collectors.joining(", "));
		System.out.println(String.format("services [%d] =>{%s}",services.keySet().size(),servicesNames));
	}
	
	public List<ServiceInfo> lookup(String service) {
		return services.get(service);
	}
	
	@Override
	protected void handle(Socket socket, InputStream fromClient, OutputStream toClient) throws IOException {
//		var sb = new StringBuilder();
		//var inString = new String(fromClient.readAllBytes(),"UTF-8");
		String inString = readFromClient(fromClient,1024);
		System.out.println(String.format("in>>> %s",inString));
		// REGISTER:service:host:port
		// LOOKUP:service
		var cmdLine= inString.split(":");
		switch (cmdLine[0].toUpperCase()) {
			case "REGISTER":
				handleRegister(cmdLine[1],cmdLine[2],Integer.parseInt(cmdLine[3].strip()));
				break;
			case "LOOKUP":
				handleLookup(cmdLine[1].strip(),toClient);
				break;
		}
//		sb.append("ciao ");
//		sb.append(inString);
//		toClient.write(sb.toString().getBytes());
	}
	
	
	private void handleLookup(String service, OutputStream toClient) throws IOException {
		System.out.println(String.format("looking for %s service",service));
		List<ServiceInfo> foundServices = lookup(service);
		var serviceInfo = foundServices.get(ThreadLocalRandom.current().nextInt(foundServices.size()));
		
		var sb = new StringBuilder();
		sb.append(String.format("%s:%d",serviceInfo.addess,serviceInfo.port));
		toClient.write(sb.toString().getBytes());
	}
	
	private void handleRegister(String service, String address, int port) {
		registerService(new ServiceInfo(service, address,port));
	}
	
}

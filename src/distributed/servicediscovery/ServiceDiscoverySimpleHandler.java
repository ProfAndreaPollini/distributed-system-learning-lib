package distributed.servicediscovery;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ServiceDiscoverySimpleHandler implements ServiceDiscoveryHandler{
	
	private ServiceDiscovery serviceDiscovery;
	
	public void setServiceDiscovery(ServiceDiscovery serviceDiscovery) {
		this.serviceDiscovery = serviceDiscovery;
	}
	
	@Override
	public void handle(InputStream fromClient, OutputStream toClient) throws IOException, ClassNotFoundException, InterruptedException {
		var sb = new StringBuilder();
		var inString = new String(fromClient.readAllBytes(),"UTF-8");
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
		sb.append("ciao ");
		sb.append(inString);
		toClient.write(sb.toString().getBytes());
		
	}
	
	private void handleLookup(String service, OutputStream toClient) throws IOException {
		System.out.println(String.format("looking for %s service",service));
		List<ServiceInfo> foundServices = serviceDiscovery.lookup(service);
		var serviceInfo = foundServices.get(ThreadLocalRandom.current().nextInt(foundServices.size()));
		
		var sb = new StringBuilder();
		sb.append(String.format("%s:%d",serviceInfo.addess,serviceInfo.port));
		toClient.write(sb.toString().getBytes());
	}
	
	private void handleRegister(String service, String address, int port) {
		serviceDiscovery.registerService(new ServiceInfo(service, address,port));
	}
}

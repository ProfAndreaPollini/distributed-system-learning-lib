package example01;

import distributed.DistributedSystem;
import distributed.broker.MessageBrokerServer;
import distributed.gateway.APIGateway;
import distributed.heartbeat.HeartbeatServer;
import distributed.servicediscovery.ServiceDiscovery;
import distributed.servicediscovery.ServiceDiscoverySimpleHandler;

import java.nio.file.Paths;

public class Example01 {
	public static void main(String[] args) {
	
		var system = new DistributedSystem("config.properties");
		var serviceDiscovery =system.addServiceDiscovery(new ServiceDiscoverySimpleHandler());
		var hs = system.addHeartbeatServer();
		var mb = system.addMessageBroker();
		var gateway = system.addAPIGateway();
		serviceDiscovery.start();
		hs.start();
		mb.start();
		
		
		//var gateway =  new APIGateway("localhost",9900);
		gateway.add(new SimpleTestHandler());
		gateway.start();
		
		try {
			serviceDiscovery.join();
			hs.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		
	}
}

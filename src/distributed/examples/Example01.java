package distributed.examples;

import distributed.broker.MessageBrokerServer;
import distributed.gateway.APIGateway;
import distributed.heartbeat.HeartbeatServer;
import distributed.servicediscovery.ServiceDiscovery;
import distributed.servicediscovery.ServiceDiscoverySimpleHandler;

public class Example01 {
	public static void main(String[] args) {
		var serviceDiscovery = new ServiceDiscovery(11000, new ServiceDiscoverySimpleHandler());
		serviceDiscovery.start();
		
		var hs = new HeartbeatServer(25000);
		hs.start();
		
		var mb = new MessageBrokerServer(20000);
		mb.start();
		
		
		var gateway =  new APIGateway("localhost",9900);
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

package distributed.examples;

import distributed.gateway.APIGateway;
import distributed.servicediscovery.ServiceDiscovery;
import distributed.servicediscovery.ServiceDiscoverySimpleHandler;

public class Example01 {
	public static void main(String[] args) {
		var serviceDiscovery = new ServiceDiscovery(11000, new ServiceDiscoverySimpleHandler());
		serviceDiscovery.run();
		
		var gateway =  new APIGateway("localhost",9900);
		gateway.add(new SimpleTestHandler());
		gateway.start();
		
		
	}
}

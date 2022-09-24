package example01;

import distributed.broker.MessageBrokerClient;
import distributed.heartbeat.HeartbeatClient;
import distributed.servicediscovery.ServiceInfo;
import distributed.servicediscovery.ServiceDiscoveryClient;

public class Node01 {
	public static void main(String[] args) {
		var sd = new ServiceDiscoveryClient("localhost",11000);
		sd.register(new ServiceInfo("TEST","localhost",12000));
		sd.register(new ServiceInfo("TEST","localhost",13000));
		sd.lookup("TEST");
		
		var mbClient = new MessageBrokerClient("localhost",20000);
		mbClient.subscribe("TEST","NODE01");
		mbClient.publish("TEST","prova");
		
		var ht = new Thread(new HeartbeatClient("localhost",25000));
		ht.start();
		var mbct = new Thread(mbClient);
		mbct.start();
		
		try {
			ht.join();
			mbct.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}

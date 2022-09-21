package distributed.examples;

import distributed.heartbeat.HeartbeatClient;
import distributed.servicediscovery.ServiceInfo;
import distributed.servicediscovery.SeviceDiscoveryClient;

public class Node01 {
	public static void main(String[] args) {
		var sd = new SeviceDiscoveryClient("localhost",11000);
		sd.register(new ServiceInfo("TEST","localhost",12000));
		sd.register(new ServiceInfo("TEST","localhost",13000));
		sd.lookup("TEST");
		var ht = new Thread(new HeartbeatClient("localhost",25000));
		ht.start();
		try {
			ht.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}

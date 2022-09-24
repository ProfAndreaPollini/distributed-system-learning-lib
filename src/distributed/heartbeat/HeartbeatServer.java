package distributed.heartbeat;

import distributed.broker.MessageBrokerClient;
import distributed.core.UDPServer;
import distributed.servicediscovery.ServiceDiscoveryClient;

import java.net.InetAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Timer;

public class HeartbeatServer extends UDPServer {
	private final HashMap<HeartbeatNodeInfo, Instant> nodesLog;
	
	private final ServiceDiscoveryClient serviceDiscoveryClient;
	private final MessageBrokerClient messageBrokerClient;
	
	//private final MessageBrokerClient messageBrokerClient;
	public HeartbeatServer(int port, int serviceDiscoveryPort,int messageBrokerPort) {
		super(port);
		messageBrokerClient = new MessageBrokerClient("localhost",messageBrokerPort);
		serviceDiscoveryClient = new ServiceDiscoveryClient("localhost",serviceDiscoveryPort);
		nodesLog = new HashMap<>();
		Runnable task = () -> {
			while(true) {
				try {
					Thread.sleep(3000);
					System.out.println("??");
					for(var node: nodesLog.keySet()) {
						if(Duration.between(nodesLog.get(node),Instant.now()).getSeconds() > 7) {
							messageBrokerClient.publish("NODE_DISCONNECTED","NODE01"); // TODO: BUG payload
						}
					}
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		};
		var t = new Thread(task);
		t.start();
	}
	

	@Override
	protected void handleMessage(String message, InetAddress address, int port) {
		System.out.println("HEARTBEAT -> " + message);
		var nodeInfo = new HeartbeatNodeInfo(address,port);
		if(!nodesLog.containsKey(nodeInfo)) {
			nodesLog.put(nodeInfo,Instant.now());
		} else {
			nodesLog.replace(nodeInfo,Instant.now());
		}
	}
}

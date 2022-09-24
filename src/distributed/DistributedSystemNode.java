package distributed;

import distributed.broker.MessageBrokerClient;
import distributed.config.ConfigLoader;
import distributed.config.ConfigProperties;
import distributed.heartbeat.HeartbeatClient;
import distributed.servicediscovery.ServiceDiscoveryClient;

import java.io.InputStream;

abstract public class DistributedSystemNode implements Runnable {
	
	private final String configFileName;
	private ConfigProperties configProperties;
	private Thread heartbeatClient;
	private ServiceDiscoveryClient serviceDiscoveryClient;
	private MessageBrokerClient messageBrokerClient;
	

	
	public DistributedSystemNode(String configFileName) {
		this.configFileName = configFileName;
		loadConfig();
		createHeartbeatClient();
		createServiceDiscoveryClient();
		createMessageBrokerClient();
	}
	
	public ConfigProperties getConfigProperties() {
		return configProperties;
	}
	
	public ServiceDiscoveryClient getServiceDiscoveryClient() {
		return serviceDiscoveryClient;
	}
	
	public MessageBrokerClient getMessageBrokerClient() {
		return messageBrokerClient;
	}
	private void createMessageBrokerClient() {
		messageBrokerClient = new MessageBrokerClient("localhost",configProperties.getInt("message_broker_port"));
		var t = new Thread(messageBrokerClient);
		t.start();
	}
	
	private void createServiceDiscoveryClient() {
		serviceDiscoveryClient = new ServiceDiscoveryClient("localhost",configProperties.getInt("service_discovery_port"));
		
	}
	
	private void createHeartbeatClient() {
		heartbeatClient = new Thread(new HeartbeatClient("localhost",configProperties.getInt("heartbeat_server_port")));
		heartbeatClient.start();
	}
	
	private void loadConfig() {
		try {
			InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(configFileName);
			configProperties = ConfigLoader.loadFromResource(resourceStream);
			
			//System.out.println(properties);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void run() {
	
	}
}

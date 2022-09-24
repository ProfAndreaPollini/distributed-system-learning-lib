package distributed;

import distributed.broker.MessageBrokerServer;
import distributed.config.ConfigLoader;
import distributed.config.ConfigProperties;
import distributed.gateway.APIGateway;
import distributed.heartbeat.HeartbeatServer;
import distributed.servicediscovery.ServiceDiscovery;
import distributed.servicediscovery.ServiceDiscoverySimpleHandler;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

public class DistributedSystem {
	private final String configFileName;
	private ConfigProperties configProperties;
	private ServiceDiscovery serviceDiscovery;
	private HeartbeatServer heartbeatServer;
	private MessageBrokerServer messageBroker;
	private APIGateway apiGateway;
	
	public DistributedSystem(String configFileName) {
		this.configFileName = configFileName;
		loadConfig();
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
	
	public ServiceDiscovery addServiceDiscovery(ServiceDiscoverySimpleHandler serviceDiscoverySimpleHandler) {
		int port = configProperties.getInt("service_discovery_port");
		serviceDiscovery = new ServiceDiscovery(port,serviceDiscoverySimpleHandler);
		return serviceDiscovery;
	}
	
	public HeartbeatServer addHeartbeatServer() {
		heartbeatServer = new HeartbeatServer(configProperties.getInt("heartbeat_server_port")
						,configProperties.getInt("service_discovery_port"),configProperties.getInt("message_broker_port"));
		return heartbeatServer;
	}
	
	
	
	public MessageBrokerServer addMessageBroker() {
		messageBroker = new MessageBrokerServer(configProperties.getInt("message_broker_port"),configProperties.getInt("service_discovery_port"));
		return messageBroker;
	}
	
	public APIGateway addAPIGateway() {
		apiGateway = new APIGateway("localhost",configProperties.getInt("api_gateway_port"));
		return apiGateway;
	}
}

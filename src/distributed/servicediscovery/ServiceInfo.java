package distributed.servicediscovery;

public class ServiceInfo {
	public String addess;
	public int port;
	public String service;
	
	public ServiceInfo(String service,String addess, int port) {
		this.addess = addess;
		this.port = port;
		this.service = service;
	}
}

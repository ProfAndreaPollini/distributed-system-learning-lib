package distributed.servicediscovery;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ServiceDiscoveryHandler {
	
		public void setServiceDiscovery(ServiceDiscovery serviceDiscovery);
		public abstract void handle(InputStream fromClient,
		                            OutputStream toClient) throws IOException, ClassNotFoundException,  InterruptedException;
	
}

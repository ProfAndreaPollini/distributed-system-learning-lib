package distributed.gateway;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class APIGateway {
	private final String address;
	private final int port;
	private final HttpServer server;
	
	public APIGateway(String address, int port) {
		this.address = address;
		this.port = port;
		try {
			server = HttpServer.create(new InetSocketAddress(address, port), 0);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void add(RouteHandler handler) {
		server.createContext(handler.getPath(),handler);
	}
	
	public void start() {
		
		//server.createContext("/test", new  MyHttpHandler());
		
		Executor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
		server.setExecutor(threadPoolExecutor);
		
		server.start();
		
		//logger.info(" Server started on port 8001");
	}
}

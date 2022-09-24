package example01;

import com.sun.net.httpserver.HttpExchange;
import distributed.gateway.RouteHandler;

import java.io.OutputStream;

public class SimpleTestHandler extends RouteHandler {
	protected SimpleTestHandler() {
		super("/test");
	}
	
	@Override
	public ReturnCode handleGet(HttpExchange rr, StringBuilder out) {
		out.append("Ciao a tutti");
		return ReturnCode.OK;
	}
}

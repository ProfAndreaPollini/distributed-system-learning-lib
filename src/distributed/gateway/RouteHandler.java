package distributed.gateway;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public abstract class RouteHandler implements HttpHandler {
	
	private final String path;
	
	protected RouteHandler(String path) {
		this.path = path;
	}
	
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		var outputStream = exchange.getResponseBody();
		var sb = new StringBuilder();
		ReturnCode returnCode = ReturnCode.OK;
		switch(exchange.getRequestMethod()) {
			case "GET":
				returnCode = handleGet(exchange,sb);
				break;
			case "POST":
				returnCode = handlePost(exchange, sb);
		}
		
		// encode HTML content
		//String htmlResponse = StringEscapeUtils.escapeHtml4(sb.toString());
		var response = sb.toString();
	
		exchange.sendResponseHeaders(returnCode.code(), response.length());
		
		outputStream.write(response.getBytes());
		
		outputStream.flush();
		outputStream.close();
	}
	
	public  ReturnCode handlePost(HttpExchange rr, StringBuilder out) {
		return ReturnCode.OK;
	}
	public  ReturnCode handleGet(HttpExchange rr, StringBuilder out){
		return ReturnCode.OK;
	}
	
	public String getPath() {
		return path;
	}
	
	public enum ReturnCode {
		OK(200), ERROR(400),SERVER_ERROR(500);
		private final int code;
		
		ReturnCode(int code) {
			this.code = code;
		}
		
		
		public final int code() {
			return code;
		}
	}
}

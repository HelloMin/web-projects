package connector;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import engine.ServletContextImpl;
import engine.servlet.HelloServlet;
import engine.servlet.IndexServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.List;

public class HttpConnector implements HttpHandler, AutoCloseable{
    final HttpServer httpServer;
    final String host;
    final int port;

    final ServletContextImpl servletContext;

    public HttpConnector(String host, int port) throws IOException, ServletException {
        this.servletContext = new ServletContextImpl();
        this.servletContext.initialize(List.of(IndexServlet.class, HelloServlet.class));
        // Start http server
        this.host = host;
        this.port = port;
        this.httpServer = HttpServer.create(new InetSocketAddress(host, port), 0, "/", this);
        this.httpServer.start();
        System.out.println("start jerrymouse http server at" + host + ":" + port);
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpExchangeAdapter adapter = new HttpExchangeAdapter(exchange);
        HttpServletResponse response = new HttpServletResponseImpl(adapter);
        HttpServletRequest request = new HttpServletRequestImpl(adapter);
        // process
        try {
            this.servletContext.process(request, response);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        System.out.println("stop jerrymouse http server at" + host + ":" + port);
        this.httpServer.stop(3);
    }
}

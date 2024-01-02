import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;

public class HttpConnector implements HttpHandler, AutoCloseable{
    public static void main(String[] args) {
        System.out.println("Hello world!");
        String host="0.0.0.0";
        int port = 8080;
        try (HttpConnector connector = new HttpConnector(host, port)) {
            for (;;) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    final HttpServer httpServer;
    final String host;
    final int port;

    public HttpConnector(String host, int port) throws  IOException {
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

        try {
            process(request, response);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String html = "<h1>Hello, " + (name == null? "world" : name) +"</h1>";
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        pw.write(html);
        pw.close();
    }

    @Override
    public void close() throws Exception {
        System.out.println("stop jerrymouse http server at" + host + ":" + port);
        this.httpServer.stop(3);
    }
}

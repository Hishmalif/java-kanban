package taskManagers;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    HttpServer server;

    public HttpTaskServer() {
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0); // Создали сервер
            server.createContext("/tasks", new HttpHandlerBackend()); // Привязали обработчик

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        server.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        server.stop(0);
        System.out.println("HTTP-сервер остановлен");
    }
}
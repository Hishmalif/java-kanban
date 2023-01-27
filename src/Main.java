import taskManagers.HttpTaskServer;

public class Main {
    public static void main(String[] args) {
        HttpTaskServer server = new HttpTaskServer();
        server.start();
    }
}
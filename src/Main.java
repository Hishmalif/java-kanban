import com.google.gson.Gson;
import taskManagers.HttpTaskManager;
import taskManagers.KVTaskClient;
import tasks.Task;

import java.io.IOException;
import java.net.URI;

public class Main {
    public static void main(String[] args) {
        new HttpTaskManager(URI.create("http://localhost:8080")).start();

        KVTaskClient client = new KVTaskClient(URI.create("http://localhost:8080"));
        Gson gson = new Gson();
        client.put("123", gson.toJson(new Task("Test1")));
        client.put("1234", gson.toJson(new Task("Test2")));
        String a = client.load("123");
        String b = client.load("1234");

        System.out.println(a);
        System.out.println(b);
    }
}
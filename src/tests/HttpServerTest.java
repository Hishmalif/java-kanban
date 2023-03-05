package tests;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskManagers.FileBackedTasksManager;
import taskManagers.HttpTaskManager;
import taskManagers.KVTaskClient;
import taskManagers.Managers;
import tasks.Epic;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpServerTest extends TaskManagerTest<FileBackedTasksManager> {
    private final KVTaskClient client;
    private final Gson gson;

    public HttpServerTest() {
        gson = new Gson();
        Managers.getDefault().start();
        client = new KVTaskClient(URI.create("http://localhost:8080"));
    }

    @BeforeEach
    public void beforeEach() {

    } // Запуск сервера

    @AfterEach()
    public void stopServer() {
        //Managers.getDefault().stop();
    } // Остановка сервера

    @Test
    public void addNewTask() {
        client.put("123", gson.toJson(new Task("Test1")));
        client.put("1234", gson.toJson(new Task("Test2")));
       String a = client.load("123");
        String b = client.load("1234");

        assertNotNull(client, "Задачи на возвращаются.");
    }

    private void requestManager(String type) {

//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(uri)
//                .GET()
//                .build();

//        try {
//           // HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            // проверяем, успешно ли обработан запрос
//            System.out.println(response.body());
//            if (response.statusCode() == 200) {
//
//            } else {
//                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode());
//            }
//        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
//            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
//                    "Проверьте, пожалуйста, адрес и повторите попытку.");
//        }
    }
}
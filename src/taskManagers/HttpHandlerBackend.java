package taskManagers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpHandlerBackend implements HttpHandler {
    Gson gson = new Gson();
    FileBackedTasksManager manager;

    public HttpHandlerBackend() {
        manager = new FileBackedTasksManager("data", false);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String type = exchange.getRequestMethod();
        String[] uri = exchange.getRequestURI().toString().split("/");
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        String response;

        switch (type) {
            case "GET":
                if (uri.length == 3 && uri[2].contains("task")) {
                    response = responseAllTasks();
                    break;
                } else if (uri.length == 4 && uri[3].contains("id=")) {
                    response = responseTask(uri[3]);
                    break;
                } else if (uri.length == 3 && uri[2].contains("history")) {
                    response = responseHistory();
                    break;
                } else if (uri.length == 2 && uri[1].contains("tasks")) {
                    response = responsePriority();
                    break;
                } else if (uri.length == 5 && uri[3].contains("epic") && uri[4].contains("id=")) {
                    response = responseEpicSubtasks(uri[4]);
                    break;
                }
            case "POST":
                if (uri.length == 3 && uri[2].contains("task")) {
                    addOrUpdateTasks(body);
                    response = "OK";
                }
            case "DELETE":
                if (uri.length == 4 && uri[3].contains("id=")) {
                    deleteTask(body);
                    response = "OK";
                    break;
                } else if (uri.length == 3 && uri[2].contains("task")) {
                    deleteAllTask();
                    response = "OK";
                    break;
                }
            default:
                response = "Problem";
                break;
        }
        exchange.sendResponseHeaders(200, 0);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private String responseAllTasks() {
        System.out.println("Print all simple tasks");
        return gson.toJson(manager.getAllTask());
    }

    private String responseTask(String uri) {
        int id = Integer.parseInt(uri.split("=")[1]);
        return gson.toJson(manager.getTask(id));
    }

    private String responseHistory() {
        return gson.toJson(manager.getHistory());
    }

    private String responsePriority() {
        return gson.toJson(manager.getPrioritizedTasks());
    }

    private String responseEpicSubtasks(String uri) {
        int id = Integer.parseInt(uri.split("=")[1]);
        return gson.toJson(manager.getListSubtaskFromEpic(id));
    }

    private void addOrUpdateTasks(String json) {
        Task task = gson.fromJson(json, Task.class);
        List<Task> allTask = manager.getAllTask();
        allTask.addAll(manager.getAllEpic());
        allTask.addAll(manager.getAllSubTask()); // Методы ALL не записываются в историю

        if (allTask.contains(task)) {
            manager.update(task.getId(), task);
        } else {
            manager.add(task);
        }
    }

    private void deleteTask(String json) {
        Task task = gson.fromJson(json, Task.class);
        manager.removeTask(task.getId());
    }

    private void deleteAllTask() {
        manager.removeTasks();
        manager.removeEpics();
        manager.removeSubTasks();
    }
}
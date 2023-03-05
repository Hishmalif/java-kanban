package taskManagers;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import tasks.Task;

import java.net.URI;
import java.util.HashMap;
import java.io.IOException;
import java.net.InetSocketAddress;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskManager extends FileBackedTasksManager implements TaskManager {
    Gson gson = new Gson();
    private final String apiToken;
    private final HttpServer server;
    private final HashMap<String, FileBackedTasksManager> data = new HashMap<>();

    public HttpTaskManager(URI uri) {
        super(uri.getHost(), false);
        apiToken = generateApiToken(); // Создали токен авторизации

        try {
            server = HttpServer.create(); // Создали сервер
            server.bind(new InetSocketAddress(uri.getHost(), uri.getPort()), 0);
            server.createContext("/tasks", new HttpHandlerBackend(uri.getHost()));
            server.createContext("/register", this::register);
            server.createContext("/save", this::save);
            server.createContext("/load", this::load); // Привязали обработчики
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        server.start();
        System.out.println("HTTP-сервер запущен!");
        System.out.println("API_TOKEN: " + apiToken);
    } // Запуск сервера

    public void stop() {
        server.stop(0);
        System.out.println("HTTP-сервер остановлен");
    } // Остановка сервера

    public void load(HttpExchange h) throws IOException {
        System.out.println("\n/load");
        try {
            if (!hasAuth(h)) {
                System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
                h.sendResponseHeaders(403, 0);
                return;
            }
            if ("GET".equals(h.getRequestMethod())) {
                String key = h.getRequestURI().getPath().substring("/load/".length());

                if (key.isEmpty()) {
                    System.out.println("Key для сохранения пустой. key указывается в пути: /save/{key}");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                if (h.getRequestURI().toString().contains("API_TOKEN=")) {
                    System.out.println(h.getRequestURI());
                    sendText(h, this.load(key));
                }
            } else {
                System.out.println("/load ждёт GET-запрос, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            h.close();
        }
    } // Получение данных

    public void save(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/save");
            if (!hasAuth(h)) {
                System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
                h.sendResponseHeaders(403, 0);
                return;
            }
            if ("POST".equals(h.getRequestMethod())) {
                String key = h.getRequestURI().getPath().substring("/save/".length());

                if (key.isEmpty()) {
                    System.out.println("Key для сохранения пустой. key указывается в пути: /save/{key}");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                String value = readText(h);

                if (value.isEmpty()) {
                    System.out.println("Value для сохранения пустой. value указывается в теле запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }

                if (data.containsKey(key)) {
                    this.save(key, value);
                } else {
                    data.put(key, new FileBackedTasksManager(key, false));
                    this.save(key, value);
                }
                System.out.println("Значение для ключа " + key + " успешно обновлено!");
                h.sendResponseHeaders(200, 0);
            } else {
                System.out.println("/save ждёт POST-запрос, а получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            h.close();
        }
    } // Сохранение данных

    public void save(String key, String json) throws InterruptedException {
        Task task = gson.fromJson(json, Task.class);
        data.get(key).add(task);
    } // Сохранение задач

    public String load(String key) throws IOException, InterruptedException {
        return gson.toJson(data.get(key).getPrioritizedTasks());
    }

    private void register(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/register");
            if ("GET".equals(h.getRequestMethod())) {
                sendText(h, apiToken);
            } else {
                System.out.println("/register ждёт GET-запрос, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    } // Регистрация

    private String generateApiToken() {
        return "" + System.currentTimeMillis();
    } // Генерация токена

    private boolean hasAuth(HttpExchange h) {
        String rawQuery = h.getRequestURI().getRawQuery();
        return rawQuery != null && (rawQuery.contains("API_TOKEN=" + apiToken) || rawQuery.contains("API_TOKEN=DEBUG"));
    }

    private String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    private void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}
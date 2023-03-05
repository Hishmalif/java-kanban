package taskManagers;

import java.net.URI;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.nio.charset.StandardCharsets.UTF_8;

public class KVTaskClient {
    private final String apiToken;
    private final String uri;
    private final HttpClient client;

    public KVTaskClient(URI uri) {
        this.uri = uri.toString();
        URI register = URI.create(this.uri + "/register");

        client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(register).GET().build();
        System.out.println(register);
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
        apiToken = response.body();
    }

    public void put(String key, String json) {
        URI save = URI.create(uri + "/save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(save)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String load(String key) {
        URI load = URI.create(uri + "/load/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(load)
                .GET()
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response.body();
    }
}
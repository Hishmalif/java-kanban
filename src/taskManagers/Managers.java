package taskManagers;

import taskManagers.historyManager.HistoryManager;
import taskManagers.historyManager.InMemoryHistoryManager;

import java.net.URI;

public class Managers {
    private Managers() {
    }

    public static HttpTaskManager getDefault() { // Получение стандартной реализации менеджера
        return new HttpTaskManager(URI.create("http://localhost:8080"));
    }

    public static FileBackedTasksManager getFilesTaskManager(String filePath, boolean isTest) {
        return new FileBackedTasksManager(filePath, isTest);
    } // Получение менеджера с сохранением данных

    public static HistoryManager getDefaultHistory() { // Получение истории задач
        return new InMemoryHistoryManager();
    }
}
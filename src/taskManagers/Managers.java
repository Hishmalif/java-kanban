package taskManagers;

import taskManagers.historyManager.HistoryManager;
import taskManagers.historyManager.InMemoryHistoryManager;

public class Managers {
    private Managers() {
    }

    public static TaskManager getDefault() { // Получение стандартной реализации менеджера
        return new InMemoryTaskManager();
    }

    public static FileBackedTasksManager getFilesTaskManager(String filePath, String logPath, boolean isTest) { // Получение менеджера с сохранением данных
        return new FileBackedTasksManager(filePath, logPath, isTest);
    }

    public static HistoryManager getDefaultHistory() { // Получение истории задач
        return new InMemoryHistoryManager();
    }
}
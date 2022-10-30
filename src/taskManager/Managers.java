package taskmanager;

public class Managers {

    private Managers() {

    }

    public static TaskManager getDefault() { // Получение стандартной реализации менеджера
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() { // Получение истории задач
        return new InMemoryHistoryManager();
    }
}
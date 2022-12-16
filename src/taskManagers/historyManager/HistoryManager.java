package taskManagers.historyManager;

import tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task); // Добавление в историю

    List<Task> getHistory(); // Получение истории

    void remove(int id); // Удаление из истории
}
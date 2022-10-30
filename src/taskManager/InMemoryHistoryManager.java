package taskmanager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final List<Task> history = new ArrayList<>();

    @Override
    public List<Task> getHistory() { // Получение истории задач
        return history;
    }

    @Override
    public void add(Task task) { // Добавление в историю
        history.add(task);

        if (history.size() > 10) {
            history.remove(0);
        }
    }
}
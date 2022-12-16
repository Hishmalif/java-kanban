package taskManagers;

import tasks.*;

import java.util.List;

public interface TaskManager {
    Task add(Task task); // Добавление задач

    Task update(int id, Task task); // Обновление простых задач

    List<Task> getAllTask(); // Получение всех задач

    List<Epic> getAllEpic(); // Получение всех эпиков

    List<SubTask> getAllSubTask(); // Получение всех подзадач

    Task getTask(int id); // Получение задачи

    void removeTasks(); // Удаление всех задач

    void removeEpics(); // Удаление всех эпиков

    void removeSubTasks(); // Удаление всех подзадач

    void removeTask(int id); // Универсальное удаление

    List<SubTask> getListSubtaskFromEpic(int idEpic); // Получение списка подзадач для эпика

    List<Task> getHistory(); // Получение истории
}
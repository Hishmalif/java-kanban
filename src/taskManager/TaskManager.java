package taskmanager;

import tasks.*;

import java.util.List;

public interface TaskManager {
    void add(Task task);
    void update(int id, Task task); // Обновление простых задач
    List<Task> getAllSimple(); // Получение всех задач

    List<Epic> getAllEpic(); // Получение всех эпиков

    List<SubTask> getAllSubTask(); // Получение всех подзадач

    Task getSimple(int id); // Получение задачи

    Epic getEpic(int id); // Получение эпика

    SubTask getSubTask(int id); // Получение подзадачи

    void removeSimpleGroup(); // Удаление всех задач

    void removeEpicGroup(); // Удаление всех эпиков

    void removeSubTaskGroup(); // Удаление всех подзадач

    void removeTask(int id); // Универсальное удаление

    List<SubTask> getListSubtaskFromEpic(int idEpic); // Получение списка подзадач для эпика
}
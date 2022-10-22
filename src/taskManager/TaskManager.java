package taskmanager;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    HashMap<Integer, Task> simpleTask = new HashMap<>();
    HashMap<Integer, Epic> epicTask = new HashMap<>();
    HashMap<Integer, SubTask> subTask = new HashMap<>();

    void addTask(Object obj) { // Универсальный импорт задач
        if (obj.getClass() != Task.class && obj.getClass() != Epic.class && obj.getClass() != SubTask.class) {
            Errors.errorMessage(3);
        }
    }

    void updateTask(int id, Object obj) { // Обновление задач
        if (obj.getClass() != Task.class && obj.getClass() != Epic.class && obj.getClass() != SubTask.class) {
            Errors.errorMessage(3);
        }
    }

    List<Object> getTaskList() { // Получение списка задач
        Errors.errorMessage(4);
        return new ArrayList<>();
    }

    Object getTasks(int id) { // Получение объектов
        Errors.errorMessage(4);
        return id;
    }

    void removeTasksGroup() { // Удаление групп задач
        Errors.errorMessage(4);
    }

    void removeTask(int id) { // Удаление задач
        Errors.errorMessage(4);
    }

    List<SubTask> getListSubtaskFromEpic(int idEpic) { // Получение списка подзадач для эпика
        ArrayList<SubTask> subTasks = new ArrayList<>();

        if (epicTask.containsKey(idEpic)) {
            for (Integer idSub : epicTask.get(idEpic).getSubTasks()) {
                subTasks.add(subTask.get(idSub));
            }
            return subTasks;
        }
        return subTasks;
    }
}
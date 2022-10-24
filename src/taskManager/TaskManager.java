package taskmanager;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private final HashMap<Integer, Task> simpleTasks = new HashMap<>();
    private final HashMap<Integer, Epic> epicTasks = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public void addSimple(Task task) { // Импорт простых задач
        if (task.getName() != null) {
            int id = generateId();

            simpleTasks.put(id, task);
            simpleTasks.get(id).setId(id);
        } else {
            Errors.errorMessage(0);
        }
    }

    public void addEpic(Epic epic) { // Создание эпиков
        if (epic.getName() != null) {
            int id = generateId();

            epicTasks.put(id, epic);
            epicTasks.get(id).setId(id);
        } else {
            Errors.errorMessage(0);
        }
    }

    public void addSubTask(SubTask subTask) { // Добавление подзадач для эпиков
        if (subTask.getName() != null && epicTasks.containsKey(subTask.getEpicTask())) {
            int id = generateId();

            subTasks.put(id, subTask);
            subTask.setId(id);
            epicTasks.get(subTask.getEpicTask()).getSubTasks().add(id);
            setEpicStatus(subTask.getEpicTask());
        } else if (subTask.getName() == null) {
            Errors.errorMessage(0);
        } else if (!epicTasks.containsKey(subTask.getEpicTask())) {
            Errors.errorMessage(1);
        }
    }

    public void updateSimple(int id, Task task) { // Обновление простых задач
        simpleTasks.put(id, task);
    }

    public void updateEpic(int id, Epic epic) { // Обновление Эпиков
        epicTasks.get(id).setName(epic.getName());
        epicTasks.get(id).setDescription(epic.getDescription());
        epicTasks.get(id).setSubTasks(epic.getSubTasks());
    }

    public void updateSubTask(int id, SubTask subTask) { // Обновление подзадач
        subTasks.put(id, subTask);
        setEpicStatus(subTask.getEpicTask());
    }

    public List<Task> getAllSimple() { // Получение всех задач
        return new ArrayList<>(simpleTasks.values());
    }

    public List<Epic> getAllEpic() { // Получение всех эпиков
        return new ArrayList<>(epicTasks.values());
    }

    public List<SubTask> getAllSubTask() { // Получение всех подзадач
        return new ArrayList<>(subTasks.values());
    }

    public Task getSimple(int id) { // Получение задачи
        if (simpleTasks.containsKey(id)) {
            return simpleTasks.get(id);
        }
        return null;
    }

    public Epic getEpic(int id) { // Получение эпика
        if (epicTasks.containsKey(id)) {
            return epicTasks.get(id);
        }
        return null;
    }

    public SubTask getSubTask(int id) { // Получение подзадачи
        if (subTasks.containsKey(id)) {
            return subTasks.get(id);
        }
        return null;
    }

    public void removeSimpleGroup() { // Удаление всех задач
        simpleTasks.clear();
    }

    public void removeEpicGroup() { // Удаление всех эпиков
        epicTasks.clear();
        subTasks.clear();
    }

    public void removeSubTaskGroup() { // Удаление всех подзадач
        subTasks.clear();
    }

    public void removeSimple(int id) { // Удаление задачи
        simpleTasks.remove(id);
    }

    public void removeEpic(int id) { // Удаление эпика
        List<SubTask> epicSubTask;

        epicTasks.remove(id);
        epicSubTask = getListSubtaskFromEpic(id);
        for (SubTask sub : epicSubTask) {
            removeSubTask(sub.getId());
        }
    }

    public void removeSubTask(int id) { // Удаление подзадачи
        subTasks.remove(id);
    }

    public List<SubTask> getListSubtaskFromEpic(int idEpic) { // Получение списка подзадач для эпика
        ArrayList<SubTask> subTask = new ArrayList<>();

        if (epicTasks.containsKey(idEpic)) {
            for (Integer idSub : epicTasks.get(idEpic).getSubTasks()) {
                subTask.add(this.subTasks.get(idSub));
            }
            return subTask;
        }
        return subTask;
    }

    private int generateId() { // Общий id для задач
        return simpleTasks.size() + epicTasks.size() + subTasks.size() + 1;
    }

    private void setEpicStatus(int idEpic) { // Метод для определение статуса у эпика
        int status = 0;
        int max = 0;

        for (SubTask sub : subTasks.values()) {
            if (sub.getEpicTask() == idEpic) {
                max += 2;
                status += getStatusId(sub);
            }
        }

        if (status == 0) {
            epicTasks.get(idEpic).setStatus("NEW");
        } else if (status == max) {
            epicTasks.get(idEpic).setStatus("DONE");
        } else {
            epicTasks.get(idEpic).setStatus("IN_PROGRESS");
        }
    }

    private int getStatusId(SubTask task) { // Преобразование статуса в id
        String status = task.getStatus().toUpperCase();
        switch (status) {
            case "NEW":
                return 0;
            case "IN_PROGRESS":
                return 1;
            case "DONE":
                return 2;
            default:
                return -1;
        }
    }
}
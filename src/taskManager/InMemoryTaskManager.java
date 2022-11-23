package taskManager;

import taskManager.historyManager.HistoryManager;
import taskManager.historyManager.InMemoryHistoryManager;
import tasks.Epic;
import tasks.Statuses;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    HistoryManager historyManager = new InMemoryHistoryManager();
    private final HashMap<Integer, Task> simpleTasks = new HashMap<>();
    private final HashMap<Integer, Epic> epicTasks = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    @Override
    public void add(Task task) { // Импорт задач
        if (task.getName() != null) {
            int id = generateId();

            if (task instanceof Epic) {
                Epic epic = (Epic) task;

                epicTasks.put(id, epic);
                epicTasks.get(id).setId(id);
            } else if (task instanceof SubTask) {
                SubTask subTask = (SubTask) task;

                subTasks.put(id, subTask);
                subTask.setId(id);
                epicTasks.get(subTask.getEpicTask()).getSubTasks().add(id);
                setEpicStatus(subTask.getEpicTask());
            } else {
                simpleTasks.put(id, task);
                simpleTasks.get(id).setId(id);
            }
        } else {
            Errors.errorMessage(0);
        }
    }

    @Override
    public void update(int id, Task task) { // Обновление задач
        if (task instanceof Epic) {
            Epic epic = (Epic) task;

            epicTasks.get(id).setName(epic.getName());
            epicTasks.get(id).setDescription(epic.getDescription());
            epicTasks.get(id).setSubTasks(epic.getSubTasks());
        } else if (task instanceof SubTask) {
            SubTask subTask = (SubTask) task;

            subTasks.put(id, subTask);
            setEpicStatus(subTask.getEpicTask());
        } else {
            simpleTasks.put(id, task);
        }
    }

    @Override
    public List<Task> getAllTask() { // Получение всех задач
        return new ArrayList<>(simpleTasks.values());
    }

    @Override
    public List<Epic> getAllEpic() { // Получение всех эпиков
        return new ArrayList<>(epicTasks.values());
    }

    @Override
    public List<SubTask> getAllSubTask() { // Получение всех подзадач
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public Task getTask(int id) { // Получение задачи
        if (simpleTasks.containsKey(id)) {
            historyManager.add(simpleTasks.get(id));
            return simpleTasks.get(id);
        }
        return null;
    }

    @Override
    public Epic getEpic(int id) { // Получение эпика
        if (epicTasks.containsKey(id)) {
            historyManager.add(epicTasks.get(id));
            return epicTasks.get(id);
        }
        return null;
    }

    @Override
    public SubTask getSubTask(int id) { // Получение подзадачи
        if (subTasks.containsKey(id)) {
            historyManager.add(subTasks.get(id));
            return subTasks.get(id);
        }
        return null;
    }

    @Override
    public void removeTasks() { // Удаление всех задач
        for (Integer i : simpleTasks.keySet()) {
            historyManager.remove(i);
        }
        simpleTasks.clear();
    }

    @Override
    public void removeEpics() { // Удаление всех эпиков
        for (Integer i : epicTasks.keySet()) {
            historyManager.remove(i);
        }
        epicTasks.clear();
        removeSubTasks();
    }

    @Override
    public void removeSubTasks() { // Удаление всех подзадач
        for (Integer i : subTasks.keySet()) {
            historyManager.remove(i);
        }
        subTasks.clear();
    }

    @Override
    public void removeTask(int id) { // Универсальное удаление
        if (simpleTasks.containsKey(id)) { // Удаление простых задач
            simpleTasks.remove(id);
        } else if (epicTasks.containsKey(id)) { //Удаление эпиков и подзадач
            List<SubTask> epicSubTask;
            epicSubTask = getListSubtaskFromEpic(id);

            for (SubTask sub : epicSubTask) {
                subTasks.remove(sub.getId());
            }
            epicTasks.remove(id);
        } else {
            int idEpic = subTasks.get(id).getEpicTask();

            subTasks.remove(id); // Удаление только подзадач
            setEpicStatus(idEpic);
        }
        historyManager.remove(id);
    }

    @Override
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
            epicTasks.get(idEpic).setStatus(Statuses.NEW);
        } else if (status == max) {
            epicTasks.get(idEpic).setStatus(Statuses.DONE);
        } else {
            epicTasks.get(idEpic).setStatus(Statuses.IN_PROGRESS);
        }
    }

    private int getStatusId(SubTask task) { // Преобразование статуса в id
        Statuses status = task.getStatus();
        switch (status) {
            case NEW:
                return 0;
            case IN_PROGRESS:
                return 1;
            case DONE:
                return 2;
            default:
                return -1;
        }
    }
}
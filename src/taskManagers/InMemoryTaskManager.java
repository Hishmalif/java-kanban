package taskManagers;

import tasks.Epic;
import tasks.Task;
import tasks.Statuses;
import tasks.SubTask;
import taskManagers.historyManager.HistoryManager;
import taskManagers.historyManager.InMemoryHistoryManager;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected HistoryManager historyManager = new InMemoryHistoryManager();
    protected final HashMap<Integer, Task> simpleTasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epicTasks = new HashMap<>();
    protected final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    @Override
    public Task add(Task task) { // Импорт задач
        if (task.getName() != null) {
            int id = generateId();
            checkCrossTaskTime(task);

            if (task.getId() < generateId() && task.getId() != 0) {
                return update(task.getId(), task);
            }

            if (task instanceof Epic) {
                Epic epic = (Epic) task;

                epicTasks.put(id, epic);
                epicTasks.get(id).setId(id);
            } else if (task instanceof SubTask) {
                SubTask subTask = (SubTask) task;

                subTasks.put(id, subTask);
                subTask.setId(id);
                epicTasks.get(subTask.getEpicTask()).getSubTasks().add(subTask);
                setEpicStatus(subTask.getEpicTask());
                setEpicTime(subTask.getEpicTask());
            } else {
                simpleTasks.put(id, task);
                simpleTasks.get(id).setId(id);
            }
        } else {
            Errors.errorMessage(0);
        }
        return task;
    }

    @Override
    public Task update(int id, Task task) { // Обновление задач
        if (containsId(id)) {
            checkCrossTaskTime(task);
            if (task instanceof Epic) {
                Epic epic = (Epic) task;

                epicTasks.get(id).setName(epic.getName());
                epicTasks.get(id).setDescription(epic.getDescription());
                epicTasks.get(id).setSubTasks(epic.getSubTasks());
            } else if (task instanceof SubTask) {
                SubTask subTask = (SubTask) task;

                subTasks.put(id, subTask);
                setEpicStatus(subTask.getEpicTask());
                setEpicTime(subTask.getEpicTask());
            } else {
                simpleTasks.put(id, task);
            }

            return task;
        } else {
            throw new IllegalArgumentException("Incorrect ID");
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
    public List<SubTask> getListSubtaskFromEpic(int idEpic) { // Получение списка подзадач для эпика
        ArrayList<SubTask> subTask = new ArrayList<>();

        if (epicTasks.containsKey(idEpic)) {
            epicTasks.get(idEpic).getSubTasks().forEach(sub -> {
                subTask.add(this.subTasks.get(sub.getId()));
                historyManager.add(this.subTasks.get(sub.getId()));
            });
            return subTask;
        }
        return subTask;
    }

    @Override
    public Task getTask(int id) { // Получение задачи
        if (simpleTasks.containsKey(id)) {
            historyManager.add(simpleTasks.get(id));
            return simpleTasks.get(id);
        } else if (epicTasks.containsKey(id)) {
            historyManager.add(epicTasks.get(id));
            return epicTasks.get(id);
        } else if (subTasks.containsKey(id)) {
            historyManager.add(subTasks.get(id));
            return subTasks.get(id);
        } else {
            throw new IllegalArgumentException("Incorrect ID");
        }
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        Set<Task> tasks = new TreeSet<>((timeOne, timeTwo) -> {
            if (timeOne.getStartTime().isEqual(timeTwo.getStartTime()) && !timeOne.equals(timeTwo)) {
                timeTwo.setStartTime(timeTwo.getStartTime().plusNanos(1));
            }
            return timeOne.getStartTime().compareTo(timeTwo.getStartTime());
        });

        // tasks.addAll(getAllEpic());
        tasks.addAll(getAllTask());
        tasks.addAll(getAllSubTask());

        return tasks;
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
        if (containsId(id)) {
            if (simpleTasks.containsKey(id)) { // Удаление простых задач
                simpleTasks.remove(id);
            } else if (epicTasks.containsKey(id)) { // Удаление эпиков и подзадач
                List<SubTask> epicSubTask = getListSubtaskFromEpic(id);

                for (SubTask sub : epicSubTask) {
                    subTasks.remove(sub.getId());
                    historyManager.remove(sub.getId());
                }
                epicTasks.remove(id);
            } else {
                int idEpic = subTasks.get(id).getEpicTask();

                subTasks.remove(id); // Удаление только подзадач
                setEpicStatus(idEpic);
                setEpicTime(idEpic);
            }

            List<Integer> num = new ArrayList<>();
            for (Task task : historyManager.getHistory()) {
                num.add(task.getId());
            }

            if (num.contains(id)) {
                historyManager.remove(id);
            }
        } else {
            throw new IllegalArgumentException("Incorrect ID");
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private int generateId() { // Общий id для задач
        return simpleTasks.size() + epicTasks.size() + subTasks.size() + 1;
    }

    private void setEpicStatus(int idEpic) { // Метод для определения статуса у эпика
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

    private void setEpicTime(int idEpic) {
        epicTasks.get(idEpic).setStartDateTime();
        epicTasks.get(idEpic).setDuration();
    }

    private void checkCrossTaskTime(Task task) {
        Set<Task> tasks = getPrioritizedTasks();
        tasks.add(task);

        Task first = null;
        Task cross = null;

        for (Task t : tasks) {
            if (first == null) {
                first = t;
                continue;
            }

            if (first.getEndTime().isAfter(t.getStartTime())) {
                cross = first;
            }

            first = t;
        }
        if (cross != null) {
            throw new IllegalArgumentException("Время задачи уже занято. Укажите другое время!");
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

    private boolean containsId(int id) {
        return simpleTasks.containsKey(id) || epicTasks.containsKey(id) || subTasks.containsKey(id);
    }
}
package taskmanager;

import tasks.SubTask;

import java.util.ArrayList;
import java.util.List;

public class SubTaskManager extends TaskManager {
    @Override
    protected void addTask(Object obj) {
        super.addTask(obj);
        if (obj.getClass() == SubTask.class) {
            int id = subTask.size() + 1;
            SubTask sub = (SubTask) obj;

            if (sub.getName() != null && epicTask.containsKey(sub.getEpicTask())) {
                subTask.put(id, sub);
                epicTask.get(sub.getEpicTask()).getSubTasks().add(id);
                setEpicStatus(sub.getEpicTask());
                Errors.errorMessage(0);
            } else if (sub.getName() == null && epicTask.containsKey(sub.getEpicTask())) {
                Errors.errorMessage(1);
            } else if (sub.getEpicTask() == 0 || !epicTask.containsKey(sub.getEpicTask())) {
                Errors.errorMessage(2);
            }
        } else {
            Errors.errorMessage(4);
        }
    }

    @Override
    public void updateTask(int id, Object obj) {
        super.updateTask(id, obj);
        if (obj.getClass() == SubTask.class) {
            SubTask sub = (SubTask) obj;
            subTask.put(id, sub);
            setEpicStatus(sub.getEpicTask());
        }
    }

    @Override
    public List<Object> getTaskList() {
        return new ArrayList<>(subTask.values());
    }

    @Override
    public void removeTasksGroup() {
        subTask.clear();
    }

    @Override
    public Object getTasks(int id) {
        if (subTask.containsKey(id)) {
            return subTask.get(id);
        }
        return null;
    }

    @Override
    public void removeTask(int id) {
        subTask.remove(id);
    }

    private void setEpicStatus(int idEpic) { // Метод для определение статуса у эпика
        int status = 0;
        int max = 0;

        for (SubTask sub : subTask.values()) {
            if (sub.getEpicTask() == idEpic) {
                max += 2;
                status += getStatusId(sub);
            }
        }

        if (status == 0) {
            epicTask.get(idEpic).setStatus("NEW");
        } else if (status == max) {
            epicTask.get(idEpic).setStatus("DONE");
        } else {
            epicTask.get(idEpic).setStatus("IN_PROGRESS");
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
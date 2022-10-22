package taskmanager;

import tasks.Epic;

import java.util.ArrayList;
import java.util.List;

public class EpicTask extends TaskManager {
    @Override
    protected void addTask(Object obj) {
        super.addTask(obj);
        if (obj.getClass() == Epic.class) {
            int id = epicTask.size() + 1;
            Epic epic = (Epic) obj;

            if (epic.getName() != null) {
                epicTask.put(id, epic);
                Errors.errorMessage(0);
            } else {
                Errors.errorMessage(1);
            }
        } else {
            Errors.errorMessage(4);
        }
    }

    @Override
    public void updateTask(int id, Object obj) {
        super.updateTask(id, obj);
        if (obj.getClass() == Epic.class) {
            Epic epic = (Epic) obj;

            epicTask.get(id).setName(epic.getName());
            epicTask.get(id).setDescription(epic.getDescription());
            epicTask.get(id).setSubTasks(epic.getSubTasks());
        } else {
            Errors.errorMessage(4);
        }
    }

    @Override
    public List<Object> getTaskList() {
        return new ArrayList<>(epicTask.values());
    }

    @Override
    public void removeTasksGroup() {
        epicTask.clear();
        subTask.clear();
    }

    @Override
    public Object getTasks(int id) {
        if (epicTask.containsKey(id)) {
            return epicTask.get(id);
        }
        return null;
    }

    @Override
    public void removeTask(int id) {
        epicTask.remove(id);
    }
}
package taskmanager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class SimpleTask extends TaskManager {
    @Override
    public void addTask(Object obj) {
        super.addTask(obj);
        if (obj.getClass() == Task.class) {
            int id = simpleTask.size() + 1;
            Task task = (Task) obj;

            if (task.getName() != null) {
                simpleTask.put(id, task);
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
        if (obj.getClass() == Task.class) {
            simpleTask.computeIfAbsent(id, k -> (Task) obj);
        } else {
            Errors.errorMessage(4);
        }
    }

    @Override
    public List<Object> getTaskList() {
        return new ArrayList<>(simpleTask.values());
    }

    @Override
    public void removeTasksGroup() {
        simpleTask.clear();
    }

    @Override
    public Object getTasks(int id) {
        if (simpleTask.containsKey(id)) {
            return simpleTask.get(id);
        }
        return null;
    }

    @Override
    public void removeTask(int id) {
        simpleTask.remove(id);
    }
}
package tests;

import tasks.Statuses;
import taskManagers.Managers;
import taskManagers.TaskManager;

public class TestMemoryManager implements TestActions {
    private final DataTask dataTask = new DataTask();
    public final TaskManager taskManager = setManagers();

    @Override
    public void testAddTasks() {
        System.out.println("Add test start");
        try {
            taskManager.add(dataTask.task);
            taskManager.add(dataTask.epicOne);
            taskManager.add(dataTask.epicTwo);
            taskManager.add(dataTask.subTaskOne);
            taskManager.add(dataTask.subTaskTwo);
            taskManager.add(dataTask.subTaskThee);
            taskManager.add(dataTask.subTaskFour);
            taskManager.add(dataTask.subTaskFive);

            boolean isCorrect = taskManager.getAllTask().size() == 1 && taskManager.getAllEpic().size() == 2
                    && taskManager.getAllSubTask().size() == 5;
            getMessageOrError(isCorrect);
        } catch (Exception e) {
            getMessageOrError(false);
            e.printStackTrace();
        }
    }

    @Override
    public void testUpdateTasks() {
        System.out.println("Update test start");
        try {
            dataTask.task.setStatus(Statuses.DONE);
            dataTask.subTaskOne.setStatus(Statuses.DONE);
            dataTask.subTaskFour.setStatus(Statuses.DONE);
            taskManager.update(1, dataTask.task);
            taskManager.update(4, dataTask.subTaskOne);
            taskManager.update(7, dataTask.subTaskFour);

            boolean isCorrect = taskManager.getTask(1).getStatus() == Statuses.DONE
                    && taskManager.getTask(2).getStatus() == Statuses.IN_PROGRESS
                    && taskManager.getTask(3).getStatus() == Statuses.IN_PROGRESS;
            getMessageOrError(isCorrect);
        } catch (Exception e) {
            getMessageOrError(false);
            e.printStackTrace();
        }
    }

    @Override
    public void testRemoveTasks() {
        System.out.println("Delete test start");
        try {
            taskManager.removeTask(1);
            taskManager.removeTask(2);
            taskManager.removeTask(7);

            boolean isCorrect = taskManager.getAllTask().isEmpty() && taskManager.getAllEpic().size() == 1
                    && taskManager.getAllSubTask().size() == 1;
            getMessageOrError(isCorrect);
        } catch (Exception e) {
            getMessageOrError(false);
            e.printStackTrace();
        }
    }

    @Override
    public void testGetHistory() {
        System.out.println("History test start");
        taskManager.getTask(3);
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(4);
        taskManager.getTask(7);
        taskManager.getTask(1);

        int size = taskManager.getHistory().size();
        boolean isCorrect = size <= 5 && size > 0;
        getMessageOrError(isCorrect);
    }

    @Override
    public void removeAll() {
        taskManager.removeTasks();
        taskManager.removeEpics();
    }

    protected TaskManager setManagers() {
        return Managers.getDefault();
    }
}
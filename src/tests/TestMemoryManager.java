package tests;

import tasks.Epic;
import tasks.Statuses;
import taskManagers.Managers;
import taskManagers.TaskManager;
import tasks.SubTask;

public class TestMemoryManager implements TestActions {
    private final DataTask dataTask = new DataTask();
    public final TaskManager taskManager = setManagers();

    @Override
    public void testAddTasks() {
        System.out.println("Add test start");
        try {
            if (!(taskManager.getAllTask().isEmpty() && taskManager.getAllEpic().isEmpty()
                    && taskManager.getAllSubTask().isEmpty())) {
                return;
            }

            dataTask.task = taskManager.add(dataTask.task);
            dataTask.epicOne = (Epic) taskManager.add(dataTask.epicOne);
            dataTask.epicTwo = (Epic) taskManager.add(dataTask.epicTwo);
            dataTask.subTaskOne = (SubTask) taskManager.add(dataTask.subTaskOne);
            dataTask.subTaskTwo = (SubTask) taskManager.add(dataTask.subTaskTwo);
            dataTask.subTaskThee = (SubTask) taskManager.add(dataTask.subTaskThee);
            dataTask.subTaskFour = (SubTask) taskManager.add(dataTask.subTaskFour);
            dataTask.subTaskFive = (SubTask) taskManager.add(dataTask.subTaskFive);

            boolean isUpload = taskManager.getAllTask().size() == 1 && taskManager.getAllEpic().size() == 2
                    && taskManager.getAllSubTask().size() == 5;
            getMessageOrError(isUpload);
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
    public void testRemoveTasks() { // Протестить
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
//        taskManager.getTask(3);
//        taskManager.getTask(1);
//        taskManager.getTask(2);
//        taskManager.getTask(4);
//        taskManager.getTask(7);
//        taskManager.getTask(1);
//        taskManager.getTask(4);
//        taskManager.getTask(4);
//        taskManager.getTask(4);
//        taskManager.getTask(4);
//        taskManager.getTask(4);

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
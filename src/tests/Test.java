package tests;

import taskManager.Managers;
import taskManager.TaskManager;
import tasks.Statuses;

public class Test {
    DataTask dataTask = new DataTask();
    TaskManager taskManager = Managers.getDefault();

    public void testAddTasks() {
        System.out.println("Add test start");
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
    }

    public void testUpdateTasks() {
        System.out.println("Update test start");
        dataTask.task.setStatus(Statuses.DONE);
        dataTask.subTaskOne.setStatus(Statuses.DONE);
        dataTask.subTaskFour.setStatus(Statuses.DONE);
        taskManager.update(1, dataTask.task);
        taskManager.update(4, dataTask.subTaskOne);
        taskManager.update(7, dataTask.subTaskFour);

        boolean isCorrect = taskManager.getTask(1).getStatus() == Statuses.DONE
                && taskManager.getEpic(2).getStatus() == Statuses.IN_PROGRESS
                && taskManager.getEpic(3).getStatus() == Statuses.IN_PROGRESS;
        getMessageOrError(isCorrect);
    }

    public void testRemoveTasks() {
        System.out.println("Delete test start");
        taskManager.removeTask(1);
        taskManager.removeTask(2);
        taskManager.removeTask(7);

        boolean isCorrect = taskManager.getAllTask().isEmpty() && taskManager.getAllEpic().size() == 1
                && taskManager.getAllSubTask().size() == 1;
        getMessageOrError(isCorrect);
    }

    public void testGetHistory() {
        System.out.println("History test start");
        taskManager.getEpic(3);
        taskManager.getTask(1);
        taskManager.getEpic(2);
        taskManager.getSubTask(4);
        taskManager.getSubTask(7);
        taskManager.getTask(1);
        taskManager.getEpic(3);
        taskManager.getSubTask(7);

        int size = Managers.getDefaultHistory().getHistory().size();
        boolean isCorrect = size <= 5 && size > 0;
        getMessageOrError(isCorrect);
    }

    private void getMessageOrError(boolean isTrue) {
        if (isTrue) {
            System.out.println(ResultTest.TEST_PASSED.getType());
        } else {
            System.out.println(ResultTest.TEST_FAILED.getType());
        }
    }
}
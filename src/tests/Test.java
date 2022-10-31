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

        if (taskManager.getAllTask().size() == 1 && taskManager.getAllEpic().size() == 2
                && taskManager.getAllSubTask().size() == 5) {
            System.out.println(ResultTest.TEST_PASSED);
        } else {
            System.out.println(ResultTest.TEST_FAILED);
        }
    }

    public void testUpdateTasks() {
        System.out.println("Update test start");
        dataTask.task.setStatus(Statuses.DONE);
        dataTask.subTaskOne.setStatus(Statuses.DONE);
        dataTask.subTaskFour.setStatus(Statuses.DONE);
        taskManager.update(1, dataTask.task);
        taskManager.update(4, dataTask.subTaskOne);
        taskManager.update(7, dataTask.subTaskFour);

        if (taskManager.getTask(1).getStatus() == Statuses.DONE
                && taskManager.getEpic(2).getStatus() == Statuses.IN_PROGRESS
                && taskManager.getEpic(3).getStatus() == Statuses.IN_PROGRESS) {
            System.out.println(ResultTest.TEST_PASSED);
        } else {
            System.out.println(ResultTest.TEST_FAILED);
        }
    }

    public void testRemoveTasks() {
        System.out.println("Delete test start");
        taskManager.removeTask(1);
        taskManager.removeTask(2);
        taskManager.removeTask(7);

        if (taskManager.getAllTask().isEmpty() && taskManager.getAllEpic().size() == 1
                && taskManager.getAllSubTask().size() == 1) {
            System.out.println(ResultTest.TEST_PASSED);
        } else {
            System.out.println(ResultTest.TEST_FAILED);
        }
    }

    public void testGetHistory() {
        taskManager.getTask(1);
        System.out.println(Managers.getDefaultHistory().getHistory());
        taskManager.getEpic(2);
        System.out.println(Managers.getDefaultHistory().getHistory());
        taskManager.getSubTask(4);
        System.out.println(Managers.getDefaultHistory().getHistory());
        taskManager.getSubTask(7);
        System.out.println(Managers.getDefaultHistory().getHistory());
        taskManager.getTask(1);
        System.out.println(Managers.getDefaultHistory().getHistory());
        taskManager.getEpic(3);
        System.out.println(Managers.getDefaultHistory().getHistory());

        if (Managers.getDefaultHistory().getHistory().size() <= 10) {
            System.out.println(ResultTest.TEST_PASSED);
        } else {
            System.out.println(ResultTest.TEST_FAILED);
        }
    }
}
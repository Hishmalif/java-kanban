package tests;

import taskmanager.TaskManager;

public class Test {
    DataTask dataTask = new DataTask();
    TaskManager taskManager = new TaskManager();

    public void addTaskToManager() {
        System.out.println("Add test start");
        taskManager.addSimple(dataTask.task);
        taskManager.addEpic(dataTask.epicOne);
        taskManager.addEpic(dataTask.epicTwo);
        taskManager.addSubTask(dataTask.subTaskOne);
        taskManager.addSubTask(dataTask.subTaskTwo);
        taskManager.addSubTask(dataTask.subTaskThee);
        taskManager.addSubTask(dataTask.subTaskFour);
        taskManager.addSubTask(dataTask.subTaskFive);

        if (taskManager.getAllSimple().size() == 1 && taskManager.getAllEpic().size() == 2
                && taskManager.getAllSubTask().size() == 5) {
            System.out.println("Add test passed");
            System.out.println(taskManager.getAllSimple());
            System.out.println(taskManager.getAllEpic());
            System.out.println(taskManager.getAllSubTask());
        } else {
            System.out.println("Check import! Test failed!");
        }
    }

    public void updateTasksFromManager() {
        System.out.println("Update test start");
        dataTask.task.setStatus("DONE");
        dataTask.subTaskOne.setStatus("DONE");
        dataTask.subTaskFour.setStatus("DONE");
        taskManager.updateSimple(1, dataTask.task);
        taskManager.updateSubTask(4, dataTask.subTaskOne);
        taskManager.updateSubTask(7, dataTask.subTaskFour);

        if (taskManager.getSimple(1).getStatus().equals("DONE")
                && taskManager.getEpic(2).getStatus().equals("IN_PROGRESS")
                && taskManager.getEpic(3).getStatus().equals("IN_PROGRESS")) {
            System.out.println("Update test passed");
            System.out.println(taskManager.getAllEpic());
        } else {
            System.out.println("Check update methods! Test failed!");
        }
    }

    public void removeTask() {
        System.out.println("Delete test start");
        taskManager.removeSimple(1);
        taskManager.removeEpic(2);
        taskManager.removeSubTask(7);

        if (taskManager.getAllSimple().isEmpty() && taskManager.getAllEpic().size() == 1
                && taskManager.getAllSubTask().size() == 4) {
            System.out.println("Delete test passed");
            System.out.println(taskManager.getAllEpic());
        } else {
            System.out.println("Check delete methods! Test failed!");
        }
    }
}
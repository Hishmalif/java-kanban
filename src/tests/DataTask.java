package tests;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

public class DataTask {
    Task task = new Task("Simple 1");
    Epic epicOne = new Epic("Epic 1");
    Epic epicTwo = new Epic("Epic 2");
    SubTask subTaskOne = new SubTask("Sub 1 EpicId 2", 2);
    SubTask subTaskTwo = new SubTask("Sub 2 EpicId 2", 2);
    SubTask subTaskThee = new SubTask("Sub 3 EpicId 2", 2);
    SubTask subTaskFour = new SubTask("Sub 1 EpicId 3", 3);
    SubTask subTaskFive = new SubTask("Sub 2 EpicId 3", 3);
}
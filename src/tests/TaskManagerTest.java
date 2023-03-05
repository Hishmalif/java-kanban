package tests;

import tasks.Task;
import tasks.Epic;
import tasks.SubTask;
import tasks.Statuses;
import taskManagers.TaskManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected Task task;
    protected Epic epic;
    protected SubTask subTaskOne;
    protected SubTask subTaskTwo;
    protected TaskManager manager;

    protected void setManager(T manager) {
        this.manager = manager;
    }

    @BeforeEach
    public void beforeEach() {
        task = new Task("Task", Statuses.IN_PROGRESS,
                15, LocalDateTime.of(2023, 12, 31, 23, 59));

        epic = new Epic("Epic", "I'm is text");

        subTaskOne = new SubTask("Sub 1", Statuses.IN_PROGRESS, 1,
                15, LocalDateTime.of(2022, 12, 31, 23, 59));

        subTaskTwo = new SubTask("Sub 2", 1,
                15, LocalDateTime.of(2023, 1, 10, 10, 0));
    }

    @AfterEach
    public void afterEach() {
        manager.removeTasks();
        manager.removeEpics();
        manager.removeSubTasks();
    }

    @Test
    public void createTask() {
        //Действие
        Task taskOne = new Task("TaskOne");
        Task taskTwo = new Task("TaskTwo", Statuses.IN_PROGRESS);
        Task taskThree = new Task("TaskThree", Statuses.DONE, "TaskDescription");
        Task taskFour = new Task("TaskFour", "DescriptionTwo");

        //Проверка
        assertEquals("TaskOne", taskOne.getName());
        assertEquals(Statuses.IN_PROGRESS, taskTwo.getStatus());
        assertEquals("TaskDescription", taskThree.getDescription());
        assertEquals("DescriptionTwo", taskFour.getDescription());
    }

    @Test
    public void createEpic() {
        //Действие
        Epic epicOne = new Epic("EpicOne");
        Task epicTwo = new Epic("EpicTwo", "EpicDescriptionTwo");

        //Проверка
        assertEquals("EpicOne", epicOne.getName());
        assertEquals("EpicDescriptionTwo", epicTwo.getDescription());
    }

    @Test
    public void createSub() {
        //Действие
        SubTask subOne = new SubTask("SubOne", 1);
        SubTask subTwo = new SubTask("SubTwo", Statuses.IN_PROGRESS, 1);
        SubTask subThree = new SubTask("TaskThree", Statuses.DONE, "Description", 1);
        SubTask subFour = new SubTask("TaskFour", "TaskDescriptionTwo", 1);

        //Проверка
        assertEquals("SubOne", subOne.getName());
        assertEquals(Statuses.IN_PROGRESS, subTwo.getStatus());
        assertEquals("Description", subThree.getDescription());
        assertEquals("TaskDescriptionTwo", subFour.getDescription());
    }

    @Test
    public void addNewTask() throws IOException, InterruptedException {
        final int taskId = manager.add(task).getId();
        final Task savedTask = manager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = manager.getAllTask();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void addNewEpic() {
        final int id = manager.add(epic).getId();
        final Task savedTask = manager.getTask(id);
        manager.add(subTaskOne);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(epic, savedTask, "Задачи не совпадают.");
        assertTrue(manager.getListSubtaskFromEpic(id).contains(subTaskOne), "Подзадачи не записываются в эпик");
        final List<Epic> tasks = manager.getAllEpic();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(epic, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void addNewSub() {
        //Подготовка
        manager.add(epic);
        assertNotNull(manager.getTask(subTaskOne.getEpicTask()));
        final int taskId = manager.add(subTaskOne).getId();
        final Task savedTask = manager.getTask(taskId);

        //Действие
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(subTaskOne, savedTask, "Задачи не совпадают.");

        final List<SubTask> tasks = manager.getAllSubTask();

        //Проверка
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(subTaskOne, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void statusInEpicWithoutSub() {
        //Действие
        manager.add(epic);

        //Проверка
        assertEquals(Statuses.NEW, manager.getTask(1).getStatus());
    }

    @Test
    public void statusInEpicWithAllSubStatusEqualNew() {
        //Подготовка
        subTaskOne.setStatus(Statuses.NEW);
        subTaskTwo.setStatus(Statuses.NEW);

        //Действие
        manager.add(epic);
        manager.add(subTaskOne);
        manager.add(subTaskTwo);

        //Проверка
        assertEquals(Statuses.NEW, manager.getTask(1).getStatus());
    }

    @Test
    public void statusInEpicWithAllSubStatusEqualDone() {
        //Подготовка
        subTaskOne.setStatus(Statuses.DONE);
        subTaskTwo.setStatus(Statuses.DONE);

        //Действие
        manager.add(epic);
        manager.add(subTaskOne);
        manager.add(subTaskTwo);

        //Проверка
        assertEquals(Statuses.DONE, manager.getTask(1).getStatus());
    }

    @Test
    public void statusInEpicWithSubStatusEqualNewAndDone() {
        //Подготовка
        subTaskOne.setStatus(Statuses.NEW);
        subTaskTwo.setStatus(Statuses.DONE);

        //Действие
        manager.add(epic);
        manager.add(subTaskOne);
        manager.add(subTaskTwo);

        //Проверка
        assertEquals(Statuses.IN_PROGRESS, manager.getTask(1).getStatus());
    }

    @Test
    public void statusInEpicWithAllSubStatusEqualProgress() {
        //Подготовка
        subTaskOne.setStatus(Statuses.IN_PROGRESS);
        subTaskTwo.setStatus(Statuses.IN_PROGRESS);

        //Действие
        manager.add(epic);
        manager.add(subTaskOne);
        manager.add(subTaskTwo);

        //Проверка
        assertEquals(Statuses.IN_PROGRESS, manager.getTask(1).getStatus());
    }

    @Test
    public void updateSimpleTask() {
        //Подготовка
        int id = manager.add(task).getId();
        task.setDescription("SimpleTask");

        //Действие
        manager.update(id, task);

        //Проверка
        assertEquals("SimpleTask", manager.getTask(id).getDescription());
    }

    @Test
    public void updateEpic() {
        int id = manager.add(epic).getId();
        epic.setDescription("EpicUpdate");

        //Действие
        manager.update(id, epic);

        //Проверка
        assertEquals("EpicUpdate", manager.getTask(id).getDescription());
    }

    @Test
    public void updateSub() {
        //Подготовка
        int idEpic = manager.add(epic).getId();
        int idSub = manager.add(subTaskOne).getId();
        subTaskOne.setStatus(Statuses.DONE);

        //Действие
        manager.update(idSub, subTaskOne);

        //Проверка
        assertEquals(Statuses.DONE, manager.getTask(idSub).getStatus());
        assertEquals(Statuses.DONE, manager.getTask(idEpic).getStatus());
    }

    @Test
    public void updateWithIncorrectId() {
        //Подготовка
        manager.add(epic);

        //Действие
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                manager.update(999, epic));

        //Проверка
        assertEquals("Incorrect ID", exception.getMessage());
    }

    @Test
    public void getAllTaskTest() {
        //Действие
        manager.add(task);

        // Проверка
        assertEquals(1, manager.getAllTask().size());
    }

    @Test
    public void getAllEpicTest() {
        //Действие
        manager.add(epic);

        //Проверка
        assertEquals(1, manager.getAllEpic().size());
    }

    @Test
    public void getAllSubTest() {
        //Действие
        manager.add(epic);
        manager.add(subTaskOne);

        //Проверка
        assertEquals(1, manager.getAllSubTask().size());
    }

    @Test
    public void getAllTaskZero() {
        assertEquals(0, manager.getAllTask().size());
    }

    @Test
    public void getOneSubFromEpic() {
        // Действие
        int id = manager.add(epic).getId();
        manager.add(subTaskTwo);

        //Проверка
        assertTrue(manager.getListSubtaskFromEpic(id).contains(subTaskTwo));
        assertFalse(manager.getListSubtaskFromEpic(id).contains(subTaskOne));
    }

    @Test
    public void getErrorFromEpicSubTask() {
        //Действие
        List<SubTask> list = manager.getListSubtaskFromEpic(999);

        //Проверка
        assertEquals(0, list.size());
    }

    @Test
    public void getSimpleTask() {
        //Действие
        int id = manager.add(task).getId();

        //Проверка
        assertNotNull(manager.getTask(id));
        assertEquals(task, manager.getTask(id));
    }

    @Test
    public void getEpic() {
        //Действие
        int id = manager.add(epic).getId();

        //Проверка
        assertNotNull(manager.getTask(id));
        assertEquals(epic, manager.getTask(id));
    }

    @Test
    public void getSub() {
        //Действие
        manager.add(epic);
        int id = manager.add(subTaskOne).getId();

        //Проверка
        assertNotNull(manager.getTask(id));
        assertEquals(subTaskOne, manager.getTask(id));
    }

    @Test
    public void getNothingFromManager() {
        //Действие
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> manager.getTask(999));

        //Проверка
        assertEquals("Incorrect ID", exception.getMessage());
    }

    @Test
    public void removeAllTasks() {
        // Подготовка
        manager.add(task);
        assertEquals(1, manager.getAllTask().size());

        //Действие
        manager.removeTasks();

        //Проверка
        assertEquals(0, manager.getAllTask().size());
    }

    @Test
    public void removeAllEpics() {
        int id = manager.add(epic).getId();
        manager.add(subTaskOne);
        assertEquals(1, manager.getAllEpic().size());
        assertTrue(manager.getListSubtaskFromEpic(id).contains(subTaskOne));
        //Действие
        manager.removeEpics();

        //Проверка
        assertFalse(manager.getListSubtaskFromEpic(id).contains(subTaskOne));
        assertEquals(0, manager.getAllEpic().size());
    }

    @Test
    public void removeAllSubs() {
        manager.add(epic);
        manager.add(subTaskOne);
        assertEquals(1, manager.getAllSubTask().size());

        //Действие
        manager.removeSubTasks();

        //Проверка
        assertEquals(0, manager.getAllSubTask().size());
    }

    @Test
    public void removeOneTask() {
        //Подготовка
        int id = manager.add(task).getId();
        assertEquals(task, manager.getTask(id));

        //Действие
        manager.removeTask(id);

        //Проверка
        assertEquals(0, manager.getAllTask().size());
    }

    @Test
    public void removeOneEpic() {
        //Подготовка
        int id = manager.add(epic).getId();
        manager.add(subTaskOne);
        assertEquals(epic, manager.getTask(id));
        assertTrue(manager.getListSubtaskFromEpic(id).contains(subTaskOne));

        //Действие
        manager.removeTask(id);

        //Проверка
        assertEquals(0, manager.getAllEpic().size());
        assertEquals(0, manager.getAllSubTask().size());
    }

    @Test
    public void removeOneSub() {
        //Подготовка
        manager.add(epic);
        int id = manager.add(subTaskOne).getId();
        assertEquals(subTaskOne, manager.getTask(id));

        //Действие
        manager.removeTask(id);

        //Проверка
        assertEquals(0, manager.getAllSubTask().size());
    }

    @Test
    public void removeIncorrectId() {
        //Действие
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                manager.removeTask(999));

        //Проверка
        assertEquals("Incorrect ID", exception.getMessage());
    }

    @Test
    public void checkEndDateInEpicWithOutTask() {
        //Подготовка
        epic = (Epic) manager.add(epic);
        final LocalDateTime result = LocalDateTime.of(1, 1, 1, 0, 0, 0);

        //Действие
        final LocalDateTime epicEndDate = epic.getEndTime();

        assertNotNull(epicEndDate);
        assertEquals(result, epicEndDate);
    }

    @Test
    public void checkEndDateInEpic() {
        //Подготовка
        epic = (Epic) manager.add(epic);
        subTaskOne = (SubTask) manager.add(subTaskOne);
        subTaskTwo = (SubTask) manager.add(subTaskTwo);
        final LocalDateTime result = LocalDateTime.of(2023, 1, 10, 10, 15);

        //Действие
        final LocalDateTime epicEndTime = epic.getEndTime();

        //Проверка
        assertNotNull(epicEndTime);
        assertEquals(result, epicEndTime);
    }

    @Test
    public void checkStartDateInTask() {
        //Подготовка
        task = manager.add(task);
        final LocalDateTime result = LocalDateTime.of(2023, 12, 31, 23, 59);

        //Действие
        final LocalDateTime taskStartTime = task.getStartTime();

        //Проверка
        assertNotNull(taskStartTime);
        assertEquals(result, taskStartTime);
    }

    @Test
    public void checkSetPrioritizedTasks() {
        //Подготовка
        epic = (Epic) manager.add(epic);
        task = manager.add(task);
        subTaskOne = (SubTask) manager.add(subTaskOne);
        subTaskTwo = (SubTask) manager.add(subTaskTwo);
        Set<Task> result = Set.of(subTaskOne, subTaskTwo, task);

        //Действие
        Set<Task> prioritizedList = (manager.getPrioritizedTasks());

        //Проверка
        assertNotNull(prioritizedList);
        assertTrue(prioritizedList.containsAll(result));
    }

    @Test
    public void checkAddTaskWithCrossTime() {
        //Подготовка
        String error = "Время задачи уже занято. Укажите другое время!";
        Task taskTwo = new Task("TaskTwo");
        task.setStartTime(LocalDateTime.of(2022, 12, 30, 10, 50));
        taskTwo.setStartTime(LocalDateTime.of(2022, 12, 30, 10, 55));
        task.setDuration(15);
        taskTwo.setDuration(10);

        //Действие
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            manager.add(task);
            manager.add(taskTwo);
        });

        //Проверка
        assertEquals(1, manager.getAllTask().size());
        assertEquals(error, exception.getMessage());
    }

//    @Test
//    public void checkCrossTasks() {
//        //Подготовка
//        InMemoryTaskManager test = new InMemoryTaskManager();
//        epic = (Epic) test.add(epic);
//        task = test.add(task);
//        subTaskOne = (SubTask) test.add(subTaskOne);
//        subTaskTwo = (SubTask) test.add(subTaskTwo);
//
//        DateTimeFormatter d = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
//        Set<Task> a = test.getPrioritizedTasks();
//        for (Task t : a) {
//            System.out.println(t.getName() + "   " + t.getStartTime().format(d) + "   " + t.getEndTime().format(d));
//        }
//
//        System.out.println(test.checkCrossTaskTime()); // List<Task>
//    }
}
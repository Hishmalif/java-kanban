package tests;

import tasks.Task;
import tasks.Epic;
import tasks.SubTask;
import tasks.Statuses;
import taskManagers.TaskManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import taskManagers.InMemoryTaskManager;
import taskManagers.historyManager.HistoryManager;
import taskManagers.historyManager.InMemoryHistoryManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {

    private Task task;
    private Epic epic;
    private SubTask subTask;
    private final HistoryManager manager = new InMemoryHistoryManager();

    @BeforeEach
    public void beforeEach() {
        TaskManager taskManager = new InMemoryTaskManager();
        task = taskManager.add(new Task("Task", "Description"));
        epic = (Epic) taskManager.add(new Epic("Epic", "EpicDescription"));
        subTask = (SubTask) taskManager.add(new SubTask("Sub", Statuses.IN_PROGRESS, 2));
    }

    @AfterEach
    public void afterEach() {
        manager.getHistory().forEach(t -> manager.remove(t.getId()));
    }

    @Test
    public void addSimpleTaskToHistory() {
        //Действие
        manager.add(task);
        final List<Task> history = manager.getHistory();

        //Проверка
        assertNotNull(history, "История пустая");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    public void addDuplicateTasks() {
        //Действие
        manager.add(task);
        manager.add(epic);
        manager.add(task);
        manager.add(subTask);
        manager.add(epic);
        manager.add(subTask);
        manager.add(task);
        final List<Task> history = manager.getHistory();

        //Проверка
        assertNotNull(history, "История пустая");
        assertEquals(3, history.size(), "История не пустая.");
    }

    @Test
    public void getHistory() {
        //Подготовка
        manager.add(epic);
        manager.add(task);
        manager.add(subTask);
        final List<Task> result = List.of(epic, task, subTask);

        //Действие
        final List<Task> history = manager.getHistory();

        //Проверка
        assertNotNull(history);
        assertEquals(3, history.size(), "Количество элементов не совпадает");
        assertEquals(result, history, "Не верный порядок в истории");
    }

    @Test
    public void removeFirstValueFromHistory() {
        //Подготовка
        manager.add(task);
        manager.add(epic);
        manager.add(subTask);
        final List<Task> result = List.of(epic, subTask);

        //Действие
        manager.remove(task.getId());

        //Проверка
        assertNotNull(manager.getHistory());
        assertEquals(2, manager.getHistory().size());
        assertEquals(result, manager.getHistory(), "Некорректное удаление первого элемента");
    }

    @Test
    public void removeLastValueFromHistory() {
        //Подготовка
        manager.add(task);
        manager.add(epic);
        manager.add(subTask);
        final List<Task> result = List.of(task, epic);

        //Действие
        manager.remove(subTask.getId());

        //Проверка
        assertNotNull(manager.getHistory());
        assertEquals(result.size(), manager.getHistory().size());
        assertEquals(result, manager.getHistory(), "Некорректное удаление последнего элемента");
    }

    @Test
    public void removeMiddleValueFromHistory() {
        //Подготовка
        manager.add(epic);
        manager.add(task);
        manager.add(subTask);
        final List<Task> result = List.of(epic, subTask);

        //Действие
        manager.remove(task.getId());

        //Проверка
        assertNotNull(manager.getHistory());
        assertEquals(2, manager.getHistory().size());
        assertEquals(result, manager.getHistory(), "Некорректное удаление среднего элемента");
    }
}
package tests;

import org.junit.jupiter.api.AfterAll;
import tasks.Epic;
import tasks.Task;

import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import taskManagers.FileBackedTasksManager;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private Task task = new Task("Task");
    private Epic epic = new Epic("Epic");

    private static final String PATH = "data/testData.csv";
    private final FileBackedTasksManager manager = new FileBackedTasksManager(PATH, true);

    public FileBackedTaskManagerTest() {
        setManager(manager);
    }

    @AfterAll
    public static void deleteFileTest() throws IOException {
        Files.deleteIfExists(Path.of(PATH));
    }

    @Test
    public void checkRecoveryFileWithoutTasks() {
        // Действие
        final int zero = manager.getAllTask().size() + manager.getAllEpic().size() + manager.getAllSubTask().size()
                + manager.getHistory().size();

        //Проверка
        assertEquals(0, zero);
    }

    @Test
    public void checkRecoveryFileTasks() {
        //Подготовка
        manager.add(task);
        manager.add(epic);

        //Действие
        manager.removeTasksWithoutRemoveInFile();
        manager.getDataFromFile();

        //Проверка
        assertNotNull(manager.getAllTask());
        assertNotNull(manager.getAllEpic());
        assertNotNull(manager.getAllSubTask());
        assertEquals(1, manager.getAllTask().size());
        assertEquals(1, manager.getAllEpic().size());
    }

    @Test
    public void checkRecoveryFileHistory() {
        //Подготовка
        task = manager.add(task);
        epic = (Epic) manager.add(epic);
        manager.getTask(epic.getId());
        manager.getTask(task.getId());

        //Действие
        manager.removeTasksWithoutRemoveInFile();
        manager.getDataFromFile();

        //Проверка
        assertNotNull(manager.getAllTask());
        assertNotNull(manager.getAllEpic());
        assertNotNull(manager.getAllSubTask());
        assertEquals(1, manager.getAllTask().size());
        assertEquals(1, manager.getAllEpic().size());
        assertEquals(2, manager.getHistory().size());
    }

    @Test
    public void checkWriteTasksToFile() {
        manager.add(task);
        final String result = "1,TASK,Task,NEW, ,0,0001.01.01 00:00:00,";

        //Действие
        final String[] fileText = getFileText().split(System.lineSeparator());

        //Проверка
        assertNotNull(fileText);
        assertEquals(result, fileText[fileText.length - 1]);
    }

    @Test
    public void checkWriteHistoryToFile() {
        task = manager.add(task);
        epic = (Epic) manager.add(epic);
        manager.getTask(task.getId());
        manager.getTask(epic.getId());
        final String result = "1,2";

        //Действие
        final String[] fileText = getFileText().split(System.lineSeparator());

        //Проверка
        assertNotNull(fileText);
        assertEquals(result, fileText[fileText.length - 1]);
    }

    private String getFileText() {
        StringBuilder builder = new StringBuilder();

        try (FileReader fileReader = new FileReader(PATH)) {
            BufferedReader reader = new BufferedReader(fileReader);
            while (reader.ready()) {
                builder.append(reader.readLine()).append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return builder.toString();
    }
}
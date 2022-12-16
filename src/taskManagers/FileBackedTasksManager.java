package taskManagers;

import taskManagers.historyManager.HistoryManager;
import tasks.Epic;
import tasks.Statuses;
import tasks.SubTask;
import tasks.Task;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private Path file;
    private Path log;

    public FileBackedTasksManager(String filePath, String logPath, boolean isTest) { // Принимаем на вход путь к файлам
        file = Paths.get(filePath);
        log = Paths.get(logPath);

        if (file.getRoot() == null) { // Если путь задан некорректно - создаем стандартный
            if (!isTest) {
                file = createDefaultPath("data.csv");
            } else {
                file = createDefaultPath("testData.csv");
            }
        }

        if (log.getRoot() == null) { // Также поступаем и с логом пользователя
            if (!isTest) {
                log = createDefaultPath("log.csv");
            } else {
                log = createDefaultPath("testLog.csv");
            }

        }

        getDataFromFile(); // Получение данных
        getLogFromFile(); // Получение логов
    }

    @Override
    public Task add(Task task) {
        super.add(task);
        save();
        return task;
    }

    @Override
    public Task update(int id, Task task) {
        super.update(id, task);
        save();
        return task;
    }

    @Override
    public void removeTasks() {
        super.removeTasks();
        save();
    }

    @Override
    public void removeEpics() {
        super.removeEpics();
        save();
    }

    @Override
    public void removeSubTasks() {
        super.removeSubTasks();
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    private void save() { // Обновление файла
        StringBuilder builder = new StringBuilder("id,type,name,status,description,epic" + System.lineSeparator());

        try (FileWriter fileWriter = new FileWriter(file.toString(), StandardCharsets.UTF_8, false)) {
            for (Task task : getAllTask()) {
                builder.append(task);
            }
            for (Epic epic : getAllEpic()) {
                builder.append(epic);
            }
            for (SubTask sub : getAllSubTask()) {
                builder.append(sub);
            }

            fileWriter.write(builder.toString());

            try (FileWriter logWriter = new FileWriter(log.toString(), StandardCharsets.UTF_8)) {
                logWriter.write(historyToString(super.historyManager));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String historyToString(HistoryManager manager){
        StringBuilder history = new StringBuilder();
        for (Task element: manager.getHistory()) {
            history.append(element.getId()).append(",");
        }
        return history.toString();
    }

    private Path createDefaultPath(String name) {
        final Path dir = Paths.get(System.getProperty("user.dir"), "data"); // Стандартная дериктория
        final Path path = Paths.get(dir.toString(), name); // Стандартный файл

        if (Files.notExists(dir)) {
            try {
                Files.createDirectory(dir); // Создаем стандартную деректорию если не была создана ранее
            } catch (IOException e) {
                new ManagerSaveException().printStackTrace(); // Сделать красиво
            }
        }

        if (Files.notExists(path)) {
            try {
                Files.createFile(path); // Создаем файл если не был создан ранее
            } catch (IOException e) {
                new ManagerSaveException().printStackTrace();
            }
        }
        return path;
    }

    private List<String> getListFromFile(String path) {
        List<String> tasks = new ArrayList<>();

        try (FileReader fileReader = new FileReader(path, StandardCharsets.UTF_8)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (bufferedReader.ready()) {
                tasks.add(bufferedReader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    private void getDataFromFile() {
        List<String> tasks = getListFromFile(file.toString());
        for (int i = 1; i < tasks.size(); i++) {
            Task task;
            String[] detail = tasks.get(i).split(",");

            if (detail[1].equals(Types.EPIC.name())) {
                task = new Epic(detail[2]);
            } else if (detail[1].equals(Types.SUBTASK.name())) {
                task = new SubTask(detail[2], Integer.parseInt(detail[5].trim()));
            } else {
                task = new Task(detail[2]);
            }

            task.setId(Integer.parseInt(detail[0]));
            task.setStatus(Statuses.getStatus(detail[3]));
            task.setDescription(detail[4]);
            add(task);
        }
    }

    private void getLogFromFile() { // Получение данных о логах
        List<String> logs = getListFromFile(log.toString());
        for (String line : logs) {
            String[] tasks = line.split(",");
            for (String id : tasks) {
                getTask(Integer.parseInt(id));
            }
        }
    }
}
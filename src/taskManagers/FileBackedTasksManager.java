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

    public FileBackedTasksManager(String filePath, boolean isTest) { // Принимаем на вход путь к файлам
        file = Paths.get(filePath);

        if (file.getRoot() == null) { // Если путь задан некорректно - создаем стандартный
            if (!isTest) {
                file = createDefaultPath("data.csv");
            } else {
                file = createDefaultPath("testData.csv");
            }
        }

        getLogFromFile(); // Получение логов
        getDataFromFile(); // Получение задач
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
    public Task getTask(int id) {
        Task task = super.getTask(id);
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

        // Собираем данные в строку
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
            builder.append(System.lineSeparator()).append(historyToString(historyManager));

            fileWriter.write(builder.toString());
        } catch (IOException e) {
            throw new RuntimeException("Error write file!");
        }
    }

    private static String historyToString(HistoryManager manager) {
        StringBuilder history = new StringBuilder();

        List<Task> element = manager.getHistory(); // ??
        for (int i = 0; i < element.size(); i++) {
            if (element.size() - 1 == i) {
                history.append(element.get(i).getId());
            } else {
                history.append(element.get(i).getId()).append(",");
            }
        }
        return history.toString();
    }

    private Path createDefaultPath(String name) {
        final Path dir = Paths.get(System.getProperty("user.dir"), "data"); // Стандартная дериктория
        final Path path = Paths.get(dir.toString(), name); // Стандартный файл

        if (Files.notExists(dir)) {
            try {
                Files.createDirectory(dir); // Создаем стандартную деректорию если не была создана ранее
                if (Files.notExists(dir)) {
                    throw new ManagerSaveException("Error create default directory");
                }
            } catch (ManagerSaveException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException("Unknown error while creating directory");
            }
        }

        if (Files.notExists(path)) {
            try {
                Files.createFile(path); // Создаем файл если не был создан ранее

                if (Files.notExists(path)) {
                    throw new ManagerSaveException("Error create default file");
                }
            } catch (ManagerSaveException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException("Unknown error while creating file");
            }
        }
        return path;
    }

    private List<String> getListFromFile() {
        List<String> tasks = new ArrayList<>();

        try (FileReader fileReader = new FileReader(file.toString(), StandardCharsets.UTF_8)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (bufferedReader.ready()) {
                tasks.add(bufferedReader.readLine());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error getting line from file");
        }
        return tasks;
    }

    private void getDataFromFile() {
        List<String> tasks = getListFromFile();

        for (int i = 1; i < tasks.size(); i++) {
            if (tasks.get(i) == null || tasks.get(i).equals("")) { // Проверяем дошли ли до истории
                break;
            }

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
            add(task); // Добавляем задачу
        }
    }

    private void getLogFromFile() { // Получение данных о логах
        List<String> logs = getListFromFile();
        int size = logs.size() > 2 ? logs.size() - 1 : 1; // Определяем положение логов

        if (logs.isEmpty() || !logs.get(size - 1).equals("") || logs.get(size).equals("")) {
            return;
        }

        String[] tasks = logs.get(logs.size() - 1).split(",");
        for (String id : tasks) {
            getTask(Integer.parseInt(id.trim()));
        }
    }
}
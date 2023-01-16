package taskManagers;

import tasks.Task;
import tasks.Epic;
import tasks.SubTask;
import tasks.Statuses;
import taskManagers.historyManager.HistoryManager;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Arrays;
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

    public void getDataFromFile() {
        List<String> tasks = getListFromFile();
        String history = tasks.size() > 2 ? tasks.get(tasks.size() - 1) : "";

        for (int i = 1; i < tasks.size() - 1; i++) {
            if (tasks.get(i).equals("")) { // Проверяем дошли ли до истории
                break;
            }

            Task task;
            String[] detail = tasks.get(i).split(",");

            if (detail[1].equals(Types.EPIC.name())) {
                task = new Epic(detail[2]);
            } else if (detail[1].equals(Types.SUBTASK.name())) {
                task = new SubTask(detail[2], Integer.parseInt(detail[7].trim()));
            } else if (detail[1].equals(Types.TASK.name())) {
                task = new Task(detail[2]);
            } else {
                new ManagerSaveException("Incorrect type task").printStackTrace();
                continue;
            }

            task.setId(Integer.parseInt(detail[0]));
            task.setStatus(Statuses.getStatus(detail[3]));
            task.setDescription(detail[4]);
            task.setDuration(Long.parseLong(detail[5]));
            task.setStartTime(LocalDateTime.parse(detail[6], DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")));
            add(task); // Добавляем задачу
        }
        getHistoryFromFile(history);
    }

    /**
     * Метод удаляет все задачи и историю, но файл не перезаписывается
     */
    public void removeTasksWithoutRemoveInFile() {
        super.historyManager.getHistory().forEach(task -> historyManager.remove(task.getId()));
        super.simpleTasks.clear();
        super.epicTasks.clear();
        super.subTasks.clear();
    }

    private void getHistoryFromFile(String history) { // Получение данных об истории
        String[] tasks = history.split(",");
        Arrays.stream(tasks)
                .filter(s -> !s.equals(""))
                .forEach(s -> getTask(Integer.parseInt(s.trim())));
    }

    private void save() { // Обновление файла
        StringBuilder builder = new StringBuilder("id,type,name,status,description,duration,startTime,epic"
                + System.lineSeparator());

        // Собираем данные в строку
        try (FileWriter fileWriter = new FileWriter(file.toString(), StandardCharsets.UTF_8, false)) {
            getAllTask().forEach(task -> builder.append(task));
            getAllEpic().forEach(epic -> builder.append(epic));
            getAllSubTask().forEach(subTask -> builder.append(subTask));
            builder.append(System.lineSeparator()).append(historyToString(historyManager));

            fileWriter.write(builder.toString());
        } catch (IOException e) {
            throw new RuntimeException("Error write file!");
        }
    }

    private static String historyToString(HistoryManager manager) {
        StringBuilder history = new StringBuilder();

        List<Task> element = manager.getHistory();
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
        final Path dir = Paths.get(System.getProperty("user.dir"), "data"); // Стандартная директория
        final Path path = Paths.get(dir.toString(), name); // Стандартный файл

        if (Files.notExists(dir)) {
            try {
                Files.createDirectory(dir); // Создаем стандартную директорию если не была создана ранее
                if (Files.notExists(dir)) {
                    throw new ManagerSaveException("Error create default directory");
                }
            } catch (ManagerSaveException e) {
                e.printStackTrace();
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
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException("Unknown error while creating file");
            }
        }
        return path;
    }

    private List<String> getListFromFile() {
        final List<String> tasks = new ArrayList<>();

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
}
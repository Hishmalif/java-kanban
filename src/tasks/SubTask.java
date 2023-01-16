package tasks;

import taskManagers.Types;

import java.util.Objects;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubTask extends Task {
    private int epicTask;

    public SubTask(String name, int epicTask) {
        super(name);
        this.epicTask = epicTask;
    }

    public SubTask(String name, String description, int epicTask) {
        super(name, description);
        this.epicTask = epicTask;
    }

    public SubTask(String name, Statuses status, int epicTask) {
        super(name, status);
        this.epicTask = epicTask;
    }

    public SubTask(String name, Statuses status, String description, int epicTask) {
        super(name, status, description);
        this.epicTask = epicTask;
    }

    public SubTask(String name, String description, Statuses status, int epicTask,
                   long duration, LocalDateTime startTime) {
        super(name, status, description, duration, startTime);
        this.epicTask = epicTask;
    }

    public SubTask(String name, String description, int epicTask, long duration, LocalDateTime startTime) {
        super(name, description, duration, startTime);
        this.epicTask = epicTask;
    }

    public SubTask(String name, Statuses status, int epicTask, long duration, LocalDateTime startTime) {
        super(name, status, duration, startTime);
        this.epicTask = epicTask;
    }

    public SubTask(String name, int epicTask, long duration, LocalDateTime startTime) {
        super(name, duration, startTime);
        this.epicTask = epicTask;
    }

    // getters and setters
    public int getEpicTask() {
        return epicTask;
    }

    public void setEpicTask(int epicTask) {
        this.epicTask = epicTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return epicTask == subTask.epicTask;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicTask);
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d,%s,%s%n", super.getId(), Types.SUBTASK.name(),
                super.getName(), super.getStatus().getName(), super.getDescription(),
                super.getDuration(),
                super.getStartTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")),
                getEpicTask());
    }
}
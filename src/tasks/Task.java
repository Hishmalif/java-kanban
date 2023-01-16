package tasks;

import taskManagers.Types;

import java.util.Objects;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private int id;
    private String name;
    private Statuses status;
    private long duration;
    private LocalDateTime startTime;
    private String description;

    public Task(String name) {
        this.name = name;
        status = Statuses.NEW;
    }

    public Task(String name, Statuses status) {
        this.name = name;
        this.status = status;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = Statuses.NEW;
    }

    public Task(String name, Statuses status, String description) {
        this.name = name;
        this.status = status;
        this.description = description;
    }

    public Task(String name, Statuses status, String description, long duration, LocalDateTime startTime) {
        this.name = name;
        this.status = status;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description, long duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
        this.status = Statuses.NEW;
    }

    public Task(String name, Statuses status, long duration, LocalDateTime startTime) {
        this.name = name;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, long duration, LocalDateTime startTime) {
        this.name = name;
        this.duration = duration;
        this.startTime = startTime;
        status = Statuses.NEW;
    }

    public LocalDateTime getEndTime() {
        return getStartTime().plusMinutes(duration);
    }

    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Statuses getStatus() {
        return status;
    }

    public void setStatus(Statuses status) {
        this.status = status;
    }

    public String getDescription() {
        return description != null ? description : " ";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime != null ? startTime :
                LocalDateTime.of(1, 1, 1, 0, 0, 0, 0);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    //Equals and hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && duration == task.duration && Objects.equals(name, task.name)
                && status == task.status && Objects.equals(startTime, task.startTime)
                && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, duration, startTime, description);
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d,%s,%n", id, Types.TASK.name(), name,
                status, getDescription(), duration,
                getStartTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")));
    }
}
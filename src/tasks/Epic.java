package tasks;

import taskManagers.Types;

import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Epic extends Task {
    private List<SubTask> subTasks = new ArrayList<>();

    public Epic(String name) {
        super(name);
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    @Override
    public LocalDateTime getEndTime() { // Расчет времени по подзадачам
        return subTasks.stream()
                .map(Task::getEndTime)
                .max(LocalDateTime::compareTo).orElse(getStartTime());
    }

    public void setStartDateTime() { // Установка минимального времени
        super.setStartTime(subTasks.stream()
                .map(Task::getStartTime)
                .min(LocalDateTime::compareTo).orElse(getStartTime()));
    }

    public void setDuration() { // Установка продолжительности задачи
        super.setDuration(subTasks.stream()
                .map(Task::getDuration)
                .reduce(Long::sum).orElse(0L));
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasks, epic.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasks);
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d,%s,%n", super.getId(), Types.EPIC.name(),
                super.getName(), super.getStatus(), getDescription(), super.getDuration(),
                super.getStartTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")));

    }
}
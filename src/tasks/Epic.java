package tasks;

import taskManagers.Types;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Integer> subTasks = new ArrayList<>();

    public Epic() {
    }

    public Epic(String name) {
        super(name);
        setStatus(Statuses.NEW);
    }

    public List<Integer> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<Integer> subTasks) {
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
        return Objects.hash(subTasks);
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%n", super.getId(), Types.EPIC.name(), super.getName(),
                super.getStatus(), getDescription());
    }
}
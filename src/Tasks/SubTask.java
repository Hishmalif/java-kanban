package tasks;

import java.util.Objects;

public class SubTask extends Task {
    private int epicTask;

    public SubTask() {

    }

    public SubTask(String name) {
        super(name);
    }

    public SubTask(String name, int epicTask) {
        super(name);
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
    public String toString() {
        return "SubTask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", epicTask=" + epicTask +
                '}';
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
}
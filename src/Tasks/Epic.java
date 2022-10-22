package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasks = new ArrayList<>();

    public Epic() {
    }

    public Epic(String name) {
        super(name);
        setStatus("NEW");
    }

    public ArrayList<Integer> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ArrayList<Integer> subTasks) {
        this.subTasks = subTasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasks=" + subTasks +
                ", name=" + getName() +
                '}';
    }
}
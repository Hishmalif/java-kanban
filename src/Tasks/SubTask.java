package tasks;

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
                "epicTask=" + epicTask +
                "status=" + getStatus() +
                '}';
    }
}
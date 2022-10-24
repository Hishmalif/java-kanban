import tests.Test;

public class Main {
    public static void main(String[] args) {
        Test test = new Test();
        test.addTaskToManager();
        test.updateTasksFromManager();
        test.removeTask();
    }
}
import tests.Test;

public class Main {
    public static void main(String[] args) {
        Test test = new Test();
        test.testAddTasks();
        test.testGetHistory();
        test.testUpdateTasks();
        test.testRemoveTasks();
    }
}
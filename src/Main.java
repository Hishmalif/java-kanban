import tests.TestFileManager;

public class Main {
    public static void main(String[] args) {
        TestFileManager test = new TestFileManager();

        try {
            test.testAddTasks();
            test.testUpdateTasks();
            test.testRemoveTasks();
            test.testGetHistory();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            test.removeAll();
        }
    }
}
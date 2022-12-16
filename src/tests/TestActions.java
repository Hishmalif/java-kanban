package tests;

public interface TestActions {
    void testAddTasks();

    void testUpdateTasks();

    void testRemoveTasks();

    void testGetHistory();

    void removeAll();

    default void getMessageOrError(boolean isCorrect) {
        if (isCorrect) {
            System.out.println(ResultTest.TEST_PASSED.getType());
        } else {
            System.out.println(ResultTest.TEST_FAILED.getType());
        }
    }
}
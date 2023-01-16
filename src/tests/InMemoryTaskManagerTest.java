package tests;

import taskManagers.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    public InMemoryTaskManagerTest() {
        setManager(new InMemoryTaskManager());
    }
}

package tests;

import taskManagers.Managers;
import taskManagers.TaskManager;

public class TestFileManager extends TestMemoryManager implements TestActions {
    @Override
    protected TaskManager setManagers() {
        return Managers.getFilesTaskManager("", "", true);
    }
}
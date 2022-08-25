package manager;

import manager.implementation.FileBackedTasksManager;
import manager.implementation.InMemoryHistoryManager;
import manager.interfaces.HistoryManager;
import manager.interfaces.TaskManager;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        return FileBackedTasksManager.loadFromFile(new File("testing.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
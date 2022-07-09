package manager;

import manager.implementation.FileBackedTasksManager;
import manager.implementation.InMemoryHistoryManager;
import manager.interfaces.HistoryManager;
import manager.interfaces.TaskManager;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        File file = new File("testing.csv");
        return new FileBackedTasksManager(file);
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
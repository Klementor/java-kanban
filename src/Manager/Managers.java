package Manager;

import Manager.implementation.FileBackedTasksManager;
import Manager.implementation.InMemoryHistoryManager;
import Manager.interfaces.HistoryManager;
import Manager.interfaces.TaskManager;

import java.nio.file.Paths;

public class Managers {

    public static TaskManager getDefault() {
        return new FileBackedTasksManager(Paths.get("testing.csv"));
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
package InMemoryTaskManager;

import InMemoryTaskManager.implementation.InMemoryHistoryManager;
import InMemoryTaskManager.implementation.InMemoryTaskManager;
import InMemoryTaskManager.interfaces.HistoryManager;
import InMemoryTaskManager.interfaces.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}

package InMemoryTaskManager.interfaces;

import model.Task;

import java.util.LinkedList;

public interface HistoryManager {

    void addHistory(Task task);
    LinkedList<Task> getHistory();
}

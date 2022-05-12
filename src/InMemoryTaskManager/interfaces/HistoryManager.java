package InMemoryTaskManager.interfaces;

import model.Task;
import java.util.LinkedList;
import java.util.List;

public interface HistoryManager {
    List<Task> list = new LinkedList<Task>();

    void addHistory(Task task);

    List<Task> getHistory();
}

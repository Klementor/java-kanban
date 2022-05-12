package InMemoryTaskManager.implementation;

import InMemoryTaskManager.interfaces.HistoryManager;
import model.Task;
import java.util.List;

import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> requestHistory;
    private static final int HISTORY_SIZE = 10;

    public InMemoryHistoryManager() {
        this.requestHistory = new LinkedList<>();
    }

    @Override
    public List<Task> getHistory() {
        return requestHistory;
    }

    @Override
    public void addHistory(Task task) {
        if (task != null) {
            if (requestHistory.size() == HISTORY_SIZE) {
                requestHistory.remove(0);
            }
            requestHistory.add(task);
        }
    }
}

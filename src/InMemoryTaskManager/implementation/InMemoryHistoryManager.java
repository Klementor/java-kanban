package InMemoryTaskManager.implementation;

import InMemoryTaskManager.interfaces.HistoryManager;
import model.Task;

import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> requestHistory;

    public InMemoryHistoryManager() {
        this.requestHistory = new LinkedList<>();
    }

    @Override
    public LinkedList<Task> getHistory() {
        return requestHistory;
    }

    @Override
    public void addHistory(Task task) {
        if (requestHistory.size() == 10) {
            requestHistory.remove(0);
        }
        requestHistory.add(task);
    }
}

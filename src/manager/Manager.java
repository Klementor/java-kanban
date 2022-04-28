package manager;

import model.*;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int i = -1;
    private final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private final HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();

    private int getId() {
        i++;
        return i;
    }

    public void addTask(Task task) {
        task.setId(getId());
        taskHashMap.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epic.setId(getId());
        epicHashMap.put(epic.getId(), epic);
    }

    public void addSubTask(SubTask subTask) {
        subTask.setId(getId());
        subTaskHashMap.put(subTask.getId(), subTask);
        getSubTaskList(subTask.getEpicId()).add(subTask.getId());
        updateStatusEpic(subTask.getEpicId());
    }

    public Task getTask(int id) {
        return taskHashMap.getOrDefault(id, null);
    }

    public Epic getEpic(int id) {
        return epicHashMap.getOrDefault(id, null);
    }

    public SubTask getSubTask(int id) {
        return subTaskHashMap.getOrDefault(id, null);
    }

    public ArrayList<Integer> getSubTaskList(int epicId) {
        return getEpic(epicId).getIdSubTasks();
    }

    public HashMap<Integer, Task> getTaskHashMap() {
        return taskHashMap;
    }

    public HashMap<Integer, Epic> getEpicHashMap() {
        return epicHashMap;
    }

    public HashMap<Integer, SubTask> getSubTaskHashMap() {
        return subTaskHashMap;
    }

    public void clearTask() {
        taskHashMap.clear();
    }

    public void clearEpic() {
        epicHashMap.clear();
        subTaskHashMap.clear();
    }

    public void clearSubTask() {
        subTaskHashMap.clear();
        for (Integer id : taskHashMap.keySet()) {
            getEpic(id).setStatus(TaskStatus.NEW);
        }
    }

    public void updateTask(Task task) {
        if (taskHashMap.containsKey(task.getId())) {
            taskHashMap.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epicHashMap.containsKey(epic.getId())) {
            epicHashMap.put(epic.getId(), epic);
        }
    }

    public void updateSubTask(SubTask subTask) {
        if (subTaskHashMap.containsKey(subTask.getId())) {
            subTaskHashMap.put(subTask.getId(), subTask);
            updateStatusEpic(subTask.getEpicId());
        }
    }

    public void removeTask(int id) {
        taskHashMap.remove(id);
    }

    public void removeEpic(int id) {
        for (int i = 0; i < getSubTaskList(id).size(); i++) {
            subTaskHashMap.remove(getSubTaskList(id).get(i));
        }
        epicHashMap.remove(id);
    }

    public void removeSubTask(int id) {
        getSubTaskList(getSubTask(id).getEpicId()).remove((Integer) id);
        updateStatusEpic(getSubTask(id).getEpicId());
        subTaskHashMap.remove(id);
    }

    public void updateStatusEpic(int id) {
        getSubTaskList(id);
        int statusInProgress = 0;
        int statusDone = 0;
        for (int i = 0; i < getSubTaskList(id).size(); i++) {
            if (getSubTask(getSubTaskList(id).get(i)).equals(TaskStatus.IN_PROGRESS)) {
                statusInProgress++;
            } else {
                statusDone++;
            }
        }
        if (statusInProgress < 1 && statusDone < 1) {
            getEpic(id).setStatus(TaskStatus.NEW);
        } else if (statusInProgress > 1) {
            getEpic(id).setStatus(TaskStatus.IN_PROGRESS);
        } else {
            getEpic(id).setStatus(TaskStatus.DONE);
        }
    }
}

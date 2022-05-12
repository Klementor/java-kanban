package InMemoryTaskManager.interfaces;

import model.*;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(SubTask subTask);

    Task getTask(int id);

    Epic getEpic(int id);

    SubTask getSubTask(int id);

    ArrayList<Integer> getSubTaskList(int epicId);

    HashMap<Integer, Task> getTasksMap();

    HashMap<Integer, Epic> getEpicsMap();

    HashMap<Integer, SubTask> getSubTasksMap();

    void clearTask();

    void clearEpic();

    void clearSubTask();

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    void removeTask(int id);

    void removeEpic(int id);

    void removeSubTask(int id);

    void updateStatusEpic(int id);

    List<Task> getHistory();
}

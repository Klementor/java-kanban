package Manager.interfaces;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;
import java.util.Map;

public interface TaskManager {

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(SubTask subTask);

    Task getTask(int id);

    Epic getEpic(int id);

    SubTask getSubTask(int id);

    List<Integer> getSubTaskList(int epicId);

    Map<Integer, Task> getTasksMap();

    Map<Integer, Epic> getEpicsMap();

    Map<Integer, SubTask> getSubTasksMap();

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
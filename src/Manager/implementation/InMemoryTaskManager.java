package Manager.implementation;

import Manager.Managers;
import Manager.interfaces.HistoryManager;
import Manager.interfaces.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static model.TaskStatus.*;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    private final Map<Integer, Task> tasksMap;
    private final Map<Integer, Epic> epicsMap;
    private final Map<Integer, SubTask> subTasksMap;
    private final HistoryManager history;

    public InMemoryTaskManager() {
        this.tasksMap = new HashMap<>();
        this.epicsMap = new HashMap<>();
        this.subTasksMap = new HashMap<>();
        this.history = Managers.getDefaultHistory();
    }

    @Override
    public void addTask(Task task) {    //Добавление в мапы всех видов задач
        task.setId(id++);
        tasksMap.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(id++);
        epicsMap.put(epic.getId(), epic);
    }

    @Override
    public void addSubTask(SubTask subTask) {
        subTask.setId(id++);
        subTasksMap.put(subTask.getId(), subTask);
        getSubTaskList(subTask.getEpicId()).add(subTask.getId());
        updateStatusEpic(subTask.getEpicId());
    }

    @Override
    public Task getTask(int id) {    //Получение задач всех видов
        if (tasksMap.getOrDefault(id, null) != null) {
            history.addHistory(tasksMap.get(id));
            return tasksMap.get(id);
        } else {
            return null;
        }
    }

    @Override
    public Epic getEpic(int id) {
        if (epicsMap.getOrDefault(id, null) != null) {
            history.addHistory(epicsMap.get(id));
            return epicsMap.get(id);
        } else {
            return null;
        }
    }

    @Override
    public SubTask getSubTask(int id) {
        if (subTasksMap.getOrDefault(id, null) != null) {
            history.addHistory(subTasksMap.get(id));
            return subTasksMap.get(id);
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<Integer> getSubTaskList(int epicId) {    //Нахождение по id Epic'а всех id subTask'ов
        if (epicsMap.getOrDefault(epicId, null) != null) {
            return epicsMap.getOrDefault(epicId, null).getIdSubTasks();
        } else {
            return null;
        }
    }

    @Override
    public HashMap<Integer, Task> getTasksMap() {    //Получение HashMap'ов всех типов задач
        return new HashMap<>(tasksMap);
    }

    @Override
    public HashMap<Integer, Epic> getEpicsMap() {
        return new HashMap<>(epicsMap);
    }

    @Override
    public HashMap<Integer, SubTask> getSubTasksMap() {
        return new HashMap<>(subTasksMap);
    }

    @Override
    public void clearTask() {    //Полная очистка HashMap'ов
        clearMemoryTask();
        tasksMap.clear();
    }

    @Override
    public void clearEpic() {    //При очистке Epic'а, нужно очистить и его subTask'и
        clearMemoryEpic();
        epicsMap.clear();
        subTasksMap.clear();
    }

    @Override
    public void clearSubTask() {    //При очистке subTask'ов нужно изменить статус Epic'а,
        // а также очистить списки idSubTask в Epic'ах
        clearMemorySubTask();
        subTasksMap.clear();
        for (Integer id : epicsMap.keySet()) {
            epicsMap.get(id).setStatus(NEW);
            epicsMap.get(id).setIdSubTasks(null);
        }
    }

    public void clearMemoryTask() {
        for (Map.Entry<Integer, Task> entry : tasksMap.entrySet()) {
            history.remove(entry.getKey());
        }
    }

    public void clearMemorySubTask() {
        for (Map.Entry<Integer, SubTask> entry : subTasksMap.entrySet()) {
            history.remove(entry.getKey());
        }
    }

    public void clearMemoryEpic() {
        for (Map.Entry<Integer, Epic> entry : epicsMap.entrySet()) {
            history.remove(entry.getKey());
        }
        clearMemorySubTask();
    }

    @Override
    public void updateTask(Task task) {    //Обновление всех типов задач
        if (tasksMap.containsKey(task.getId())) {
            tasksMap.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epicsMap.containsKey(epic.getId())) {
            epicsMap.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTasksMap.containsKey(subTask.getId())) {
            subTasksMap.put(subTask.getId(), subTask);
            updateStatusEpic(subTask.getEpicId());
        }
    }

    @Override
    public void removeTask(int id) {    //Удаление одной конкретной задачи
        if (tasksMap.getOrDefault(id, null) != null) {
            tasksMap.remove(id);
            history.remove(id);
        }
    }

    @Override
    public void removeEpic(int id) {    //При удалении Epic'а, нужно удалить и все принадлежащие ему подзадачи
        if (getSubTaskList(id) != null) {
            for (int i = 0; i < getSubTaskList(id).size(); i++) {
                subTasksMap.remove(getSubTaskList(id).get(i));
                history.remove(getSubTaskList(id).get(i));
            }
            epicsMap.remove(id);
            history.remove(id);
        }
    }

    @Override
    public void removeSubTask(int id) {     //При удалении подзадачи, нужно сделать проверку статуса Epic'а
        if (getSubTaskList(subTasksMap.getOrDefault(id, null).getEpicId()) != null) {
            getSubTaskList(subTasksMap.getOrDefault(id, null).getEpicId()).remove((Integer) id);
            updateStatusEpic(subTasksMap.getOrDefault(id, null).getEpicId());
            subTasksMap.remove(id);
            history.remove(id);
        }
    }

    @Override
    public void updateStatusEpic(int id) {    //Обновление статуса Epic'а
        int statusInProgress = 0;
        int statusDone = 0;
        if (getSubTaskList(id) != null) {
            for (int i = 0; i < getSubTaskList(id).size(); i++) {
                if (subTasksMap.getOrDefault(getSubTaskList(id).get(i), null) != null) {
                    if (subTasksMap.getOrDefault((getSubTaskList(id).get(i)), null).getStatus() == IN_PROGRESS) {
                        statusInProgress++;
                    } else {
                        statusDone++;
                    }
                }
            }
            if (statusInProgress < 1 && statusDone < 1) {
                epicsMap.get(id).setStatus(NEW);
            } else if (statusInProgress > 1) {
                epicsMap.get(id).setStatus(IN_PROGRESS);
            } else {
                epicsMap.get(id).setStatus(DONE);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }
}
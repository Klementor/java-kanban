package manager;

import model.*;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int id = 0;
    private final HashMap<Integer, Task> tasksMap;
    private final HashMap<Integer, Epic> epicsMap;
    private final HashMap<Integer, SubTask> subTasksMap;

    public Manager() {
        this.tasksMap = new HashMap<>();
        this.epicsMap = new HashMap<>();
        this.subTasksMap = new HashMap<>();
    }

    public void addTask(Task task) {    //Добавление в мапы всех видов задач
        task.setId(id++);
        tasksMap.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epic.setId(id++);
        epicsMap.put(epic.getId(), epic);
    }

    public void addSubTask(SubTask subTask) {
        subTask.setId(id++);
        subTasksMap.put(subTask.getId(), subTask);
        getSubTaskList(subTask.getEpicId()).add(subTask.getId());
        updateStatusEpic(subTask.getEpicId());
    }

    public Task getTask(int id) {    //Получение задач всех видов
        return tasksMap.getOrDefault(id, null);
    }

    public Epic getEpic(int id) {
        return epicsMap.getOrDefault(id, null);
    }

    public SubTask getSubTask(int id) {
        return subTasksMap.getOrDefault(id, null);
    }

    public ArrayList<Integer> getSubTaskList(int epicId) {    //Нахождение по id Epic'а всех id subTask'ов
        if (getEpic(epicId) != null) {
            return getEpic(epicId).getIdSubTasks();
        } else {
            return null;
        }
    }

    public HashMap<Integer, Task> getTasksMap() {    //Получение HashMap'ов всех типов задач
        return tasksMap;
    }

    public HashMap<Integer, Epic> getEpicsMap() {
        return epicsMap;
    }

    public HashMap<Integer, SubTask> getSubTasksMap() {
        return subTasksMap;
    }

    public void clearTask() {    //Полная очистка HashMap'ов
        tasksMap.clear();
    }

    public void clearEpic() {    //При очистке Epic'а, нужно очистить и его subTask'и
        epicsMap.clear();
        subTasksMap.clear();
    }

    public void clearSubTask() {    //При очистке subTask'ов нужно изменить статус Epic'а,
        // а также очистить списки idSubTask в Epic'ах
        subTasksMap.clear();
        for (Integer id : epicsMap.keySet()) {
            getEpic(id).setStatus(TaskStatus.NEW);
            getEpic(id).setIdSubTasks(null);
        }
    }

    public void updateTask(Task task) {    //Обновление всех типов задач
        if (tasksMap.containsKey(task.getId())) {
            tasksMap.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epicsMap.containsKey(epic.getId())) {
            epicsMap.put(epic.getId(), epic);
        }
    }

    public void updateSubTask(SubTask subTask) {
        if (subTasksMap.containsKey(subTask.getId())) {
            subTasksMap.put(subTask.getId(), subTask);
            updateStatusEpic(subTask.getEpicId());
        }
    }

    public void removeTask(int id) {    //Удаление одной конкретной задачи
        if (getTask(id) != null) {
            tasksMap.remove(id);
        }
    }

    public void removeEpic(int id) {    //При удалении Epic'а, нужно удалить и все принадлежащие ему подзадачи
        if (getSubTaskList(id) != null) {
            for (int i = 0; i < getSubTaskList(id).size(); i++) {
                subTasksMap.remove(getSubTaskList(id).get(i));
            }
            epicsMap.remove(id);
        }
    }

    public void removeSubTask(int id) {     //При удалении подзадачи, нужно сделать проверку статуса Epic'а
        if (getSubTaskList(getSubTask(id).getEpicId()) != null) {
            getSubTaskList(getSubTask(id).getEpicId()).remove((Integer) id);
            updateStatusEpic(getSubTask(id).getEpicId());
            subTasksMap.remove(id);
        }
    }

    public void updateStatusEpic(int id) {    //Обновление статуса Epic'а
        int statusInProgress = 0;
        int statusDone = 0;
        if (getSubTaskList(id) != null) {
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
}

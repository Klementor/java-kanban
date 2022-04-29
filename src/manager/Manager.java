package manager;

import model.*;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int i = 0;
    private final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private final HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();

    private int getFirstId() {
        return i++;
    }

    public void addTask(Task task) {    //Добавление в мапы всех видов задач
        task.setId(getFirstId());
        taskHashMap.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epic.setId(getFirstId());
        epicHashMap.put(epic.getId(), epic);
    }

    public void addSubTask(SubTask subTask) {
        subTask.setId(getFirstId());
        subTaskHashMap.put(subTask.getId(), subTask);
        getSubTaskList(subTask.getEpicId()).add(subTask.getId());
        updateStatusEpic(subTask.getEpicId());
    }

    public Task getTask(int id) {    //Получение задач всех видов
        return taskHashMap.getOrDefault(id, null);
    }

    public Epic getEpic(int id) {
        return epicHashMap.getOrDefault(id, null);
    }

    public SubTask getSubTask(int id) {
        return subTaskHashMap.getOrDefault(id, null);
    }

    public ArrayList<Integer> getSubTaskList(int epicId) {    //Нахождение по id Epic'а всех id subTask'ов
        return getEpic(epicId).getIdSubTasks();
    }

    public HashMap<Integer, Task> getTaskHashMap() {    //Получение HashMap'ов всех типов задач
        return taskHashMap;
    }

    public HashMap<Integer, Epic> getEpicHashMap() {
        return epicHashMap;
    }

    public HashMap<Integer, SubTask> getSubTaskHashMap() {
        return subTaskHashMap;
    }

    public void clearTask() {    //Полная очистка HashMap'ов
        taskHashMap.clear();
    }

    public void clearEpic() {    //При очистке Epic'а, нужно очистить и его subTask'и
        epicHashMap.clear();
        subTaskHashMap.clear();
    }

    public void clearSubTask() {    //При очистке subTask'ов нужно изменить статус Epic'а,
        // а также очистить списки idSubTask в Epic'ах
        subTaskHashMap.clear();
        for (Integer id : epicHashMap.keySet()) {
            getEpic(id).setStatus(String.valueOf(TaskStatus.NEW));
            getEpic(id).setIdSubTasks(null);
        }
    }

    public void updateTask(Task task) {    //Обновление всех типов задач
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

    public void removeTask(int id) {    //Удаление одной конкретной задачи, эпика или подзадачи
        taskHashMap.remove(id);
    }

    public void removeEpic(int id) {    //При удалении Epic'а, нужно удалить и все принадлежащие ему подзадачи
        for (int i = 0; i < getSubTaskList(id).size(); i++) {
            subTaskHashMap.remove(getSubTaskList(id).get(i));
        }
        epicHashMap.remove(id);
    }

    public void removeSubTask(int id) {     //При удалении подзадачи, нужно сделать проверку статуса Epic'а
        getSubTaskList(getSubTask(id).getEpicId()).remove((Integer) id);
        updateStatusEpic(getSubTask(id).getEpicId());
        subTaskHashMap.remove(id);
    }

    public void updateStatusEpic(int id) {    //Обновление статуса Epic'а
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
            getEpic(id).setStatus(String.valueOf(TaskStatus.NEW));
        } else if (statusInProgress > 1) {
            getEpic(id).setStatus(String.valueOf(TaskStatus.IN_PROGRESS));
        } else {
            getEpic(id).setStatus(String.valueOf(TaskStatus.DONE));
        }
    }
}

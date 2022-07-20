package manager.implementation;

import manager.Managers;
import manager.interfaces.HistoryManager;
import manager.interfaces.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static model.TaskStatus.*;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    private final Map<Integer, Task> tasksMap;
    private final Map<Integer, Epic> epicsMap;
    private final Map<Integer, SubTask> subTasksMap;
    private final HistoryManager history;
    private final TreeSet<Task> listOfTasksSortedByTime;
    private final List<Task> listOfTaskWithoutStartTime;

    public InMemoryTaskManager() {
        this.tasksMap = new HashMap<>();
        this.epicsMap = new HashMap<>();
        this.subTasksMap = new HashMap<>();
        this.history = Managers.getDefaultHistory();
        this.listOfTasksSortedByTime = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        listOfTaskWithoutStartTime = new ArrayList<>();
    }

    @Override
    public void addTask(Task task) {    //Добавление в мапы всех видов задач
        task.setId(id++);
        tasksMap.put(task.getId(), task);
        if (task.getStartTime() == null) {
            listOfTaskWithoutStartTime.add(task);
        } else {
            comparisonOfTasksOverTime(task);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(id++);
        epicsMap.put(epic.getId(), epic);
        if (epic.getStartTime() == null) {
            listOfTaskWithoutStartTime.add(epic);
        } else {
            comparisonOfTasksOverTime(epic);
        }
    }

    @Override
    public void addSubTask(SubTask subTask) {
        if (!epicsMap.containsKey(subTask.getEpicId())) {
            throw new RuntimeException("Не существует указанного Эпика");
        }
        subTask.setId(id++);
        subTasksMap.put(subTask.getId(), subTask);
        getSubTaskList(subTask.getEpicId()).add(subTask.getId());
        updateStatusEpic(subTask.getEpicId());
        startTimeForEpic(subTask.getEpicId());
        getEndTime(subTask.getEpicId());
        sumOfDuration(subTask.getEpicId());
        if (subTask.getStartTime() == null) {
            listOfTaskWithoutStartTime.add(subTask);
        } else {
            comparisonOfTasksOverTime(subTask);
        }
    }

    @Override
    public Task getTask(int id) {    //Получение задач всех видов
        if (tasksMap.get(id) != null) {
            history.addHistory(tasksMap.get(id));
            return tasksMap.get(id);
        } else {
            return null;
        }
    }

    @Override
    public Epic getEpic(int id) {
        if (epicsMap.get(id) != null) {
            history.addHistory(epicsMap.get(id));
            return epicsMap.get(id);
        } else {
            return null;
        }
    }

    @Override
    public SubTask getSubTask(int id) {
        if (subTasksMap.get(id) != null) {
            history.addHistory(subTasksMap.get(id));
            return subTasksMap.get(id);
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<Integer> getSubTaskList(int epicId) {    //Нахождение по id Epic'а всех id subTask'ов
        if (epicsMap.get(epicId) != null) {
            return epicsMap.get(epicId).getIdSubTasks();
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
        for (Task task : tasksMap.values()){
            listOfTasksSortedByTime.remove(task);
            listOfTaskWithoutStartTime.remove(task);
        }
        clearMemoryTask();
        tasksMap.clear();
    }

    @Override
    public void clearEpic() {    //При очистке Epic'а, нужно очистить и его subTask'и
        for (Epic epic : epicsMap.values()){
            listOfTasksSortedByTime.remove(epic);
            listOfTaskWithoutStartTime.remove(epic);
        }
        clearMemoryEpic();
        epicsMap.clear();
        subTasksMap.clear();
    }

    @Override
    public void clearSubTask() {    //При очистке subTask'ов нужно изменить статус Epic'а,
        // а также очистить списки idSubTask в Epic'ах
        for (SubTask subTask : subTasksMap.values()){
            listOfTasksSortedByTime.remove(subTask);
            listOfTaskWithoutStartTime.remove(subTask);
        }
        clearMemorySubTask();
        subTasksMap.clear();
        for (Integer id : epicsMap.keySet()) {
            epicsMap.get(id).setStatus(NEW);
            epicsMap.get(id).setIdSubTasks(null);
            epicsMap.get(id).setStartTime(null);
            epicsMap.get(id).setDuration(null);
            epicsMap.get(id).setEndTime(null);
        }
    }

    private void clearMemoryTask() {
        for (Map.Entry<Integer, Task> entry : tasksMap.entrySet()) {
            history.remove(entry.getKey());
        }
    }

    private void clearMemorySubTask() {
        for (Map.Entry<Integer, SubTask> entry : subTasksMap.entrySet()) {
            history.remove(entry.getKey());
        }
    }

    private void clearMemoryEpic() {
        for (Map.Entry<Integer, Epic> entry : epicsMap.entrySet()) {
            history.remove(entry.getKey());
        }
        clearMemorySubTask();
    }

    @Override
    public void updateTask(Task task) {    //Обновление всех типов задач
        if (tasksMap.containsKey(task.getId())) {
            listOfTasksSortedByTime.remove(task);
            tasksMap.put(task.getId(), task);
            comparisonOfTasksOverTime(task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epicsMap.containsKey(epic.getId())) {
            epicsMap.put(epic.getId(), epic);
            listOfTasksSortedByTime.remove(epic);
            comparisonOfTasksOverTime(epic);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTasksMap.containsKey(subTask.getId())) {
            subTasksMap.put(subTask.getId(), subTask);
            updateStatusEpic(subTask.getEpicId());
            listOfTasksSortedByTime.remove(subTask);
            comparisonOfTasksOverTime(subTask);
            startTimeForEpic(subTask.getEpicId());
            sumOfDuration(subTask.getEpicId());
            getEndTime(subTask.getEpicId());
        }
    }

    @Override
    public void removeTask(int id) {    //Удаление одной конкретной задачи
        if (tasksMap.get(id) != null) {
            listOfTasksSortedByTime.remove(tasksMap.get(id));//может выкидывать исключения, возможно потребуется проверка
            listOfTaskWithoutStartTime.remove(tasksMap.get(id));
            tasksMap.remove(id);
            history.remove(id);
        }
    }

    @Override
    public void removeEpic(int id) {    //При удалении Epic'а, нужно удалить и все принадлежащие ему подзадачи
        if (getSubTaskList(id) != null) {
            listOfTasksSortedByTime.remove(epicsMap.get(id));//может выкидывать исключения, возможно потребуется проверка
            listOfTaskWithoutStartTime.remove(epicsMap.get(id));
            for (int i = 0; i < getSubTaskList(id).size(); i++) {
                listOfTasksSortedByTime.remove(subTasksMap.get(id));//может выкидывать исключения, возможно потребуется проверка
                listOfTaskWithoutStartTime.remove(subTasksMap.get(id));
                subTasksMap.remove(getSubTaskList(id).get(i));
                history.remove(getSubTaskList(id).get(i));
            }
            epicsMap.remove(id);
            history.remove(id);
        }
    }

    @Override
    public void removeSubTask(int id) {     //При удалении подзадачи, нужно сделать проверку статуса Epic'а
        if (getSubTaskList(subTasksMap.get(id).getEpicId()) != null) {
            listOfTasksSortedByTime.remove(subTasksMap.get(id));//может выкидывать исключения, возможно потребуется проверка
            listOfTaskWithoutStartTime.remove(subTasksMap.get(id));
            getSubTaskList(subTasksMap.get(id).getEpicId()).remove((Integer) id);
            updateStatusEpic(subTasksMap.get(id).getEpicId());
            subTasksMap.remove(id);
            history.remove(id);
        }
    }

    private void updateStatusEpic(int id) {    //Обновление статуса Epic'а
        int statusInProgress = 0;
        int statusDone = 0;
        int statusNew = 0;
        if (getSubTaskList(id) != null) {
            for (int i = 0; i < getSubTaskList(id).size(); i++) {
                if (subTasksMap.get(getSubTaskList(id).get(i)) != null) {
                    if (subTasksMap.get((getSubTaskList(id).get(i))).getStatus() == IN_PROGRESS) {
                        statusInProgress++;
                    } else if (subTasksMap.get(getSubTaskList(id).get(i)).getStatus() == DONE) {
                        statusDone++;
                    } else {
                        statusNew++;
                    }
                }
            }
            if (statusInProgress < 1 && statusDone < 1) {
                epicsMap.get(id).setStatus(NEW);
            } else if (statusInProgress > 0 || (statusDone > 0 && statusNew > 0)) {
                epicsMap.get(id).setStatus(IN_PROGRESS);
            } else {
                epicsMap.get(id).setStatus(DONE);
            }
        }
    }

    public void startTimeForEpic(int epicId) {
        getEpicForMetod(epicId).getIdSubTasks()
                .stream()
                .map(this::getSubtaskForMetod)
                .map(SubTask::getStartTime)
                .min(LocalDateTime::compareTo)
                .ifPresent(getEpicForMetod(id)::setStartTime);
    }

    public void sumOfDuration(int epicId) {
        List<Integer> subTasks = getSubTaskList(epicId);
        Duration duration = Duration.ZERO;
        for (Integer subTask : subTasks) {
            Duration durationSubTask = getSubTask(subTask).getDuration();
            duration = duration.plus(durationSubTask);
        }
        epicsMap.get(epicId).setDuration(duration);
    }

    public void getEndTime(int epicId) {
        getEpicForMetod(epicId).getIdSubTasks()
                .stream()
                .map(this::getSubtaskForMetod)
                .map(x -> x.getEndTime().orElse(x.getStartTime()))
                .max(LocalDateTime::compareTo)
                .ifPresent(getEpicForMetod(id)::setEndTime);
    }

    public List<Task> getListOfTasksSortedByTime() {
        return new ArrayList<>(listOfTasksSortedByTime);
    }

    public void comparisonOfTasksOverTime(Task myTask) {
        listOfTasksSortedByTime.add(myTask);
        LocalDateTime prev = LocalDateTime.MIN;
        for (Task prioritizedTask : listOfTasksSortedByTime) {
            if (prev.isAfter(prioritizedTask.getStartTime())) {
                listOfTasksSortedByTime.remove(myTask);
                throw new RuntimeException("Произошло наложение задач, ввеленная задача будет удалена");
            }
            prev = prioritizedTask.getEndTime().orElse(prioritizedTask.getStartTime());
        }
    }

    public Epic getEpicForMetod(int id) {
        return epicsMap.get(id);
    }

    public SubTask getSubtaskForMetod(int id) {
        return subTasksMap.get(id);
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    public HistoryManager getHistoryManager() {
        return history;
    }
}
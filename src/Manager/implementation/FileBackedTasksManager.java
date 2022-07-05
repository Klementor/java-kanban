package Manager.implementation;

import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static model.TaskType.SUBTASK;
import static model.TaskType.TASK;

public class FileBackedTasksManager extends InMemoryTaskManager {
    Path path;

    public FileBackedTasksManager(Path path) {
        this.path = path;
        if (Files.exists(path)) {
            loadFromFile(path);
        } else {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save() {
        addHeader();
        try (Writer writer = new FileWriter(path.toString(), StandardCharsets.UTF_8, true)) {
            for (Map.Entry<Integer, Task> entry : getTasksMap().entrySet()) {
                writer.write(getTaskString(entry.getValue()));
                writer.write("\n");
            }
            for (Map.Entry<Integer, Epic> entry : getEpicsMap().entrySet()) {
                writer.write(getTaskString(entry.getValue()));
                writer.write("\n");
            }
            for (Map.Entry<Integer, SubTask> entry : getSubTasksMap().entrySet()) {
                writer.write(getTaskString(entry.getValue()));
                writer.write("\n");
            }
            writer.write("\n");
            writer.write(getHistoryString(getHistory()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public void loadFromFile(Path path) {
        try (Reader reader = new FileReader(path.toString(), StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                String str = bufferedReader.readLine();
                if (str != null && str.isEmpty()) {
                    String history = bufferedReader.readLine();
                    historyToList(history);
                    return;
                }
                addTaskType(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void historyToList(String history) {
        if (history == null || history.equals("")) {
            return;
        }
        String[] historyList = history.split(",");
        Map<Integer, Task> taskMap = getTasksMap();
        Map<Integer, SubTask> subTaskMap = getSubTasksMap();
        for (String task : historyList) {
            int taskId = Integer.parseInt(task);
            if (taskMap.containsKey(taskId)) {
                super.getTask(taskId);
            } else if (subTaskMap.containsKey(taskId)) {
                super.getSubTask(taskId);
            } else {
                super.getEpic(taskId);
            }
        }
    }

    public void addTaskType(String str) {
        String[] taskArray = str.split(",");
        String type = taskArray[1];
        TaskType TYPE = TaskType.valueOf(type);
        if (TYPE.equals(TASK)) {
            super.addTask(getTaskFromString(taskArray));
        } else if (TYPE.equals(SUBTASK)) {
            super.addSubTask(getSubTaskFromString(taskArray));
        } else {
            super.addEpic(getEpicFromString(taskArray));
        }
    }

    public Task getTaskFromString(String[] mass) {
        return new Task(mass[2], mass[4], TaskStatus.valueOf(mass[3]));
    }

    public SubTask getSubTaskFromString(String[] mass) {
        return new SubTask(mass[2], mass[4], TaskStatus.valueOf(mass[3]), Integer.parseInt(mass[5]));
    }

    public Epic getEpicFromString(String[] mass) {
        return new Epic(mass[2], mass[4], TaskStatus.valueOf(mass[3]));
    }

    public String getHistoryString(List<Task> list) {
        if (!list.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Task task : list) {
                stringBuilder.append(task.getId());
                stringBuilder.append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            return stringBuilder.toString();
        }
        return "";
    }

    public String getTaskString(Task task) {
        return String.join(",", new String[]{
                Integer.toString(task.getId()),
                Task.TYPE.name(),
                task.getTitle(),
                task.getStatus().name(),
                task.getDescription()
        });
    }

    public String getTaskString(Epic epic) {
        return String.join(",", new String[]{
                Integer.toString(epic.getId()),
                Epic.TYPE.name(),
                epic.getTitle(),
                epic.getStatus().name(),
                epic.getDescription()
        });
    }

    public String getTaskString(SubTask subTask) {
        return String.join(",", new String[]{
                Integer.toString(subTask.getId()),
                SubTask.TYPE.name(),
                subTask.getTitle(),
                subTask.getStatus().name(),
                subTask.getDescription(),
                Integer.toString(subTask.getEpicId())
        });
    }

    private void addHeader() {
        try (Writer writer = new FileWriter(path.toString(), StandardCharsets.UTF_8, false)) {
            writer.write(getHeader() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getHeader() {
        StringBuilder str = new StringBuilder();
        namesOfColumn[] columns = namesOfColumn.values();
        for (namesOfColumn column : columns) {
            str.append(column.name());
            str.append(",");
        }
        str.deleteCharAt(str.length() - 1);
        return str.toString();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeSubTask(int id) {
        super.removeSubTask(id);
        save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public void clearTask() {    //Полная очистка HashMap'ов
        super.clearTask();
        save();
    }

    @Override
    public void clearEpic() {    //При очистке Epic'а, нужно очистить и его subTask'и
        super.clearEpic();
        save();
    }

    @Override
    public void clearSubTask() {
        super.clearSubTask();
        save();
    }

    @Override
    public Task getTask(int id) {    //Получение задач всех видов
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = super.getSubTask(id);
        save();
        return subTask;
    }
}


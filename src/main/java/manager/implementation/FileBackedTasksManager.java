package manager.implementation;

import manager.interfaces.HistoryManager;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static model.TaskType.SUBTASK;
import static model.TaskType.TASK;

public class FileBackedTasksManager extends InMemoryTaskManager {
    Path path;

    private FileBackedTasksManager(File file) {
        this.path = file.toPath();
    }

    public void save() {
        WorkWithHeader.addHeader(path);
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
            writer.write(toString(getHistoryManager()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        if (Files.exists(file.toPath())) {
            try (Reader reader = new FileReader(file.toPath().toString(), StandardCharsets.UTF_8);
                 BufferedReader bufferedReader = new BufferedReader(reader)) {
                bufferedReader.readLine();
                while (bufferedReader.ready()) {
                    String str = bufferedReader.readLine();
                    if (str != null && str.isEmpty()) {
                        String history = bufferedReader.readLine();
                        List<Integer> tasks = fromString(history);
                        Map<Integer, Task> taskMap = fileBackedTasksManager.getTasksMap();
                        Map<Integer, SubTask> subTaskMap = fileBackedTasksManager.getSubTasksMap();
                        if (tasks != null) {
                            for (Integer taskId : tasks) {
                                if (taskMap.containsKey(taskId)) {
                                    fileBackedTasksManager.getTaskSuper(taskId);
                                } else if (subTaskMap.containsKey(taskId)) {
                                    fileBackedTasksManager.getSubTaskSuper(taskId);
                                } else {
                                    fileBackedTasksManager.getEpicSuper(taskId);
                                }
                            }
                        }
                    } else {
                        fileBackedTasksManager.addTaskType(str);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Files.createFile(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileBackedTasksManager;
    }

    private void getTaskSuper(int id) {
        super.getTask(id);
    }

    private void getEpicSuper(int id) {
        super.getEpic(id);
    }

    private void getSubTaskSuper(int id) {
        super.getSubTask(id);
    }

    public static String toString(HistoryManager manager) {
        List<Task> tasks = manager.getHistory();
        String task = tasks.stream()
                .mapToInt(Task::getId)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));
        return task;
    }

    public static List<Integer> fromString(String value) {
        List<Integer> tasks = new ArrayList<>();
        if (value == null) {
            return null;
        }
        String[] historyList = value.split(",");

        for (String task : historyList) {
            tasks.add(Integer.parseInt(task));
        }
        return tasks;
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
        if (mass[6].equals(" ")){
            return new Task(mass[2],
                    mass[4],
                    TaskStatus.valueOf(mass[3]),
                    null,
                    null);
        }
        return new Task(mass[2],
                mass[4],
                TaskStatus.valueOf(mass[3]),
                LocalDateTime.parse(mass[6], DateTimeFormatter.ISO_DATE_TIME),
                Duration.ofMinutes(Integer.parseInt(mass[5])));
    }

    public SubTask getSubTaskFromString(String[] mass) {
        if (mass[6].equals(" ")){
            return new SubTask(mass[2],
                    mass[4],
                    TaskStatus.valueOf(mass[3]),
                    Integer.parseInt(mass[5]),
                    null,
                    null);
        }
        return new SubTask(mass[2],
                mass[4],
                TaskStatus.valueOf(mass[3]),
                Integer.parseInt(mass[5]),
                LocalDateTime.parse(mass[6], DateTimeFormatter.ISO_DATE_TIME),
                Duration.ofMinutes(Integer.parseInt(mass[5])));
    }

    public Epic getEpicFromString(String[] mass) {
        if(mass[6].equals(" ")){
            return new Epic(mass[2],
                    mass[4],
                    TaskStatus.valueOf(mass[3]),
                    null,
                    null);
        }
        return new Epic(mass[2],
                mass[4],
                TaskStatus.valueOf(mass[3]),
                LocalDateTime.parse(mass[6], DateTimeFormatter.ISO_DATE_TIME),
                Duration.ofMinutes(Integer.parseInt(mass[5])));
    }

    public String getTaskString(Task task) {
        if( task.getStartTime() == null) {
            return String.join(",", new String[]{
                    Integer.toString(task.getId()),
                    Task.TYPE.name(),
                    task.getTitle(),
                    task.getStatus().name(),
                    task.getDescription(),
                    " ",
                    " "
            });
        }
        return String.join(",", new String[]{
                Integer.toString(task.getId()),
                Task.TYPE.name(),
                task.getTitle(),
                task.getStatus().name(),
                task.getDescription(),
                String.valueOf(task.getDuration()),
                task.getStartTime().format(DateTimeFormatter.ISO_DATE_TIME)
        });
    }

    public String getTaskString(Epic epic) {
        if (epic.getStartTime() == null){
            return String.join(",", new String[]{
                    Integer.toString(epic.getId()),
                    Epic.TYPE.name(),
                    epic.getTitle(),
                    epic.getStatus().name(),
                    epic.getDescription(),
                    " ",
                    " "
            });
        }
        return String.join(",", new String[]{
                Integer.toString(epic.getId()),
                Epic.TYPE.name(),
                epic.getTitle(),
                epic.getStatus().name(),
                epic.getDescription(),
                String.valueOf(epic.getDuration()),
                epic.getStartTime().format(DateTimeFormatter.ISO_DATE_TIME)
        });
    }

    public String getTaskString(SubTask subTask) {
        if (subTask.getStartTime() == null){
            return String.join(",", new String[]{
                    Integer.toString(subTask.getId()),
                    SubTask.TYPE.name(),
                    subTask.getTitle(),
                    subTask.getStatus().name(),
                    subTask.getDescription(),
                    Integer.toString(subTask.getEpicId()),
                    " ",
                    " "
            });
        }
        return String.join(",", new String[]{
                Integer.toString(subTask.getId()),
                SubTask.TYPE.name(),
                subTask.getTitle(),
                subTask.getStatus().name(),
                subTask.getDescription(),
                Integer.toString(subTask.getEpicId()),
                String.valueOf(subTask.getDuration()),
                subTask.getStartTime().format(DateTimeFormatter.ISO_DATE_TIME)
        });
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


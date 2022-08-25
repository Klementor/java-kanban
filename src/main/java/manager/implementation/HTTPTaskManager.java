package manager.implementation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import manager.servers.KVTaskClient;
import model.Epic;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class HTTPTaskManager extends FileBackedTasksManager {
    private URI uri;
    private KVTaskClient kvTaskClient;
    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    public HTTPTaskManager(URI KVServerUri) throws IOException, InterruptedException {
        super(KVServerUri);
        try {
            loadFromKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        kvTaskClient = new KVTaskClient(uri);
        this.uri = KVServerUri;
    }

    @Override
    public void save() {
        try {
            kvTaskClient.put("Task", gson.toJson(getTasksMap()));
            kvTaskClient.put("Epic", gson.toJson(getEpicsMap()));
            kvTaskClient.put("Subtask", gson.toJson(getSubTasksMap()));
            kvTaskClient.put("History", gson.toJson(getHistory()));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void loadFromKey() throws IOException, InterruptedException {
        Map<Integer, Task> tasks = gson.fromJson(kvTaskClient.load("Task"),
                new TypeToken<Map<Integer, Task>>() {
                }.getType());
        Map<Integer, Epic> epics = gson.fromJson(kvTaskClient.load("Epic"),
                new TypeToken<Map<Integer, Epic>>() {
                }.getType());
        Map<Integer, SubTask> subTasks = gson.fromJson(kvTaskClient.load("SubTask"),
                new TypeToken<Map<Integer, SubTask>>() {
                }.getType());
        List<Task> history = gson.fromJson(kvTaskClient.load("History"),
                new TypeToken<List<Task>>() {
                }.getType());
        for (Map.Entry<Integer, Task> task : tasks.entrySet()) {
            addTask(task.getValue());
        }
        for (Map.Entry<Integer, Epic> epic : epics.entrySet()) {
            addEpic(epic.getValue());
        }
        for (Map.Entry<Integer, SubTask> subTask : subTasks.entrySet()) {
            addSubTask(subTask.getValue());
        }
        for (Task task : history) {
            int taskId = task.getId();
            if (tasks.containsKey(taskId)) {
                getTaskSuper(taskId);
            } else if (subTasks.containsKey(taskId)) {
                getSubTaskSuper(taskId);
            } else {
                getEpicSuper(taskId);
            }
        }
    }
}


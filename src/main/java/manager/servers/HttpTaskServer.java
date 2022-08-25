package manager.servers;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.implementation.FileBackedTasksManager;
import manager.interfaces.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";
    HttpServer httpServer;
    File file = new File("testing.csv");
    FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
    private static TaskManager taskManager = Managers.getDefault();
    private static Gson gson = new GsonBuilder().setPrettyPrinting()
            .serializeNulls().create();

    public void startServer() {
        try {
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks/history", new HistoryHandler());
            httpServer.createContext("/tasks/task", new TaskHandler());
            httpServer.createContext("/tasks/epic", new EpicHandler());
            httpServer.createContext("/tasks/subtasks", new SubtaskHandler());
            httpServer.createContext("/tasks", new PrioritizedTasksHandler());
            httpServer.start();
        } catch (IOException e) {
            System.out.println("Произошла ошибка запроса");
        }
    }

    public void stopServer() {
        httpServer.stop(0);
    }

    static class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            URI path = httpExchange.getRequestURI();
            String stringPath = path.toString();
            String method = httpExchange.getRequestMethod();
            try {
                switch (method) {
                    case GET:
                        if (stringPath.equals("/tasks/history")) {
                            List<Task> history = taskManager.getHistory();
                            String response = gson.toJson(history);
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream outputStream = httpExchange.getResponseBody()) {
                                outputStream.write(response.getBytes());
                            }
                        } else
                            throw new RuntimeException("Неверный путь");
                        break;
                    default:
                        throw new RuntimeException("Вызвали не GET у history");
                }
            } catch (Exception e) {
                httpExchange.sendResponseHeaders(400, 0);
                httpExchange.close();
            }
        }
    }

    static class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            URI path = httpExchange.getRequestURI();
            String stringPath = path.toString();
            String method = httpExchange.getRequestMethod();
            try {
                switch (method) {
                    case GET:
                        if (stringPath.equals("/tasks/task/")) {
                            Map<Integer, Task> tasksMap = taskManager.getTasksMap();
                            String response = gson.toJson(tasksMap);
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream outputStream = httpExchange.getResponseBody()) {
                                outputStream.write(response.getBytes());
                            }
                        } else if (stringPath.startsWith("/tasks/task/?id=")) {
                            String[] id = stringPath.split("=");
                            Task task = taskManager.getTask(Integer.parseInt(id[1]));
                            String response = gson.toJson(task);
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream outputStream = httpExchange.getResponseBody()) {
                                outputStream.write(response.getBytes());
                            }
                        } else
                            throw new RuntimeException("Неверный путь");
                        break;
                    case POST:
                        InputStream inputStream = httpExchange.getRequestBody();
                        String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                        JsonElement jsonElement = JsonParser.parseString(jsonString);
                        if (!jsonElement.isJsonObject()) {
                            throw new RuntimeException("Это не jsonObject");
                        }
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        Task task = gson.fromJson(jsonObject, Task.class);
                        Map<Integer, Task> taskMap = taskManager.getTasksMap();
                        if (httpExchange.getRequestURI().toString().equals("/tasks/task/")) {
                            if (taskMap.containsValue(task)) {
                                taskManager.updateTask(task);
                            } else {
                                taskManager.addTask(task);
                            }
                        }
                        httpExchange.sendResponseHeaders(201, 0);
                        httpExchange.close();
                        break;
                    case DELETE:
                        if ("/tasks/task/".equals(stringPath)) {
                            taskManager.clearTask();
                        } else {
                            if (stringPath.startsWith("/tasks/task/?id=")) {
                                String[] mass = stringPath.split("=");
                                taskManager.removeTask(Integer.parseInt(mass[1]));
                            }
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                        httpExchange.close();
                        break;
                    default:
                        throw new RuntimeException("Вызвали не GET у task");
                }
            } catch (Throwable e) {
                httpExchange.sendResponseHeaders(400, 0);
                httpExchange.close();
            }
        }
    }

    static class EpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            URI path = httpExchange.getRequestURI();
            String stringPath = path.toString();
            String method = httpExchange.getRequestMethod();
            try {
                switch (method) {
                    case GET:
                        if (stringPath.equals("/tasks/epic/")) {
                            Map<Integer, Epic> epicsMap = taskManager.getEpicsMap();
                            String response = gson.toJson(epicsMap);
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream outputStream = httpExchange.getResponseBody()) {
                                outputStream.write(response.getBytes());
                            }
                        } else if (stringPath.startsWith("/tasks/epic/?id=")) {
                            String[] id = stringPath.split("=");
                            Epic epic = taskManager.getEpic(Integer.parseInt(id[1]));
                            String response = gson.toJson(epic);
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream outputStream = httpExchange.getResponseBody()) {
                                outputStream.write(response.getBytes());
                            }
                        } else
                            throw new RuntimeException("Неверный путь");
                        break;
                    case POST:
                        InputStream inputStream = httpExchange.getRequestBody();
                        String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                        JsonElement jsonElement = JsonParser.parseString(jsonString);
                        if (!jsonElement.isJsonObject()) {
                            throw new RuntimeException("Это не jsonObject");
                        }
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        Epic epic = gson.fromJson(jsonObject, Epic.class);
                        Map<Integer, Epic> epicMap = taskManager.getEpicsMap();
                        if (httpExchange.getRequestURI().toString().equals("/tasks/task/")) {
                            if (epicMap.containsValue(epic)) {
                                taskManager.updateTask(epic);
                            } else {
                                taskManager.addTask(epic);
                            }
                        }
                        httpExchange.sendResponseHeaders(201, 0);
                        httpExchange.close();
                        break;
                    case DELETE:
                        if ("/tasks/epic/".equals(stringPath)) {
                            taskManager.clearEpic();
                        } else {
                            if (stringPath.startsWith("/tasks/epic/?id=")) {
                                String[] mass = stringPath.split("=");
                                taskManager.removeEpic(Integer.parseInt(mass[1]));
                            }
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                        httpExchange.close();
                        break;
                    default:
                        throw new RuntimeException("Вызвали не GET у epic");
                }
            } catch (Throwable e) {
                httpExchange.sendResponseHeaders(400, 0);
                httpExchange.close();
            }
        }
    }

    static class SubtaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            URI path = httpExchange.getRequestURI();
            String stringPath = path.toString();
            String method = httpExchange.getRequestMethod();
            try {
                switch (method) {
                    case GET:
                        if (stringPath.equals("/tasks/subtask/")) {
                            Map<Integer, SubTask> subTaskMap = taskManager.getSubTasksMap();
                            String response = gson.toJson(subTaskMap);
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream outputStream = httpExchange.getResponseBody()) {
                                outputStream.write(response.getBytes());
                            }
                        } else if (stringPath.startsWith("/tasks/subtask/?id=")) {
                            String[] id = stringPath.split("=");
                            SubTask subTask = taskManager.getSubTask(Integer.parseInt(id[1]));
                            String response = gson.toJson(subTask);
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream outputStream = httpExchange.getResponseBody()) {
                                outputStream.write(response.getBytes());
                            }
                        } else if (stringPath.startsWith("/tasks/subtask/epic?id=")) {
                            String[] id = stringPath.split("=");
                            List<Integer> listSubTasks = taskManager.getSubTaskList(Integer.parseInt(id[1]));
                            String response = gson.toJson(listSubTasks);
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream outputStream = httpExchange.getResponseBody()) {
                                outputStream.write(response.getBytes());
                            }
                        } else
                            throw new RuntimeException("Неверный путь");
                        break;
                    case POST:
                        InputStream inputStream = httpExchange.getRequestBody();
                        String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                        JsonElement jsonElement = JsonParser.parseString(jsonString);
                        if (!jsonElement.isJsonObject()) {
                            throw new RuntimeException("Это не jsonObject");
                        }
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        SubTask subTask = gson.fromJson(jsonObject, SubTask.class);
                        Map<Integer, SubTask> subTaskMap = taskManager.getSubTasksMap();
                        if (httpExchange.getRequestURI().toString().equals("/tasks/subtask/")) {
                            if (subTaskMap.containsValue(subTask)) {
                                taskManager.updateSubTask(subTask);
                            } else {
                                taskManager.addSubTask(subTask);
                            }
                        }
                        httpExchange.sendResponseHeaders(201, 0);
                        httpExchange.close();
                        break;
                    case DELETE:
                        if ("/tasks/subtask/".equals(stringPath)) {
                            taskManager.clearSubTask();
                        } else {
                            if (stringPath.startsWith("/tasks/subtask/?id=")) {
                                String[] mass = stringPath.split("=");
                                taskManager.removeSubTask(Integer.parseInt(mass[1]));
                            }
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                        httpExchange.close();
                        break;
                    default:
                        throw new RuntimeException("Вызвали не GET у epic");
                }
            } catch (Throwable e) {
                httpExchange.sendResponseHeaders(400, 0);
                httpExchange.close();
            }
        }
    }

    static class PrioritizedTasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            URI path = httpExchange.getRequestURI();
            String stringPath = path.toString();
            String method = httpExchange.getRequestMethod();
            try {
                switch (method) {
                    case GET:
                        if (stringPath.equals("/tasks/")) {
                            List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
                            String response = gson.toJson(prioritizedTasks);
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream outputStream = httpExchange.getResponseBody()) {
                                outputStream.write(response.getBytes());
                            }
                        } else
                            throw new RuntimeException("Неверный путь");
                        break;
                    default:
                        throw new RuntimeException("Вызвали не GET у task");
                }
            } catch (Throwable e) {
                httpExchange.sendResponseHeaders(400, 0);
                httpExchange.close();
            }
        }
    }

}

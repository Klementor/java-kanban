package manager.implementation;

import manager.interfaces.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    public T taskManager;

    abstract T createTaskManager();

    @BeforeEach
    public void updateTakManager() {
        taskManager = createTaskManager();
    }

    @Test
    public void testingForEpicAndSubTask() {
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Выйти на улицу", "Будем спортсменами и спустимся по лестнице",
                TaskStatus.NEW, 0, LocalDateTime.of(2022, 12, 21, 10, 25), Duration.ofMinutes(10));
        SubTask subTask1 = new SubTask("a", "b", TaskStatus.NEW, 0,
                LocalDateTime.of(2022, 12, 21, 10, 45), Duration.ofMinutes(15));
        taskManager.addSubTask(subTask);
        taskManager.addSubTask(subTask1);
        TaskStatus epicsStatus = taskManager.getEpic(0).getStatus();
        assertEquals(epicsStatus.toString(), TaskStatus.NEW.toString());
        int idEpic = subTask.getEpicId();
        int idEpic1 = subTask1.getEpicId();
        assertTrue(taskManager.getEpicsMap().containsKey(idEpic));
        assertTrue(taskManager.getEpicsMap().containsKey(idEpic1));
        assertEquals(LocalDateTime.of(2022, 12, 21, 10, 25), subTask.getStartTime());
        assertEquals(Duration.ofMinutes(10), subTask.getDuration());
        assertEquals(LocalDateTime.of(2022, 12, 21, 10, 45), subTask1.getStartTime());
        assertEquals(Duration.ofMinutes(15), subTask1.getDuration());
    }

    @Test
    public void testingAddTest() {
        Task task = new Task("а", "b", LocalDateTime.of(2022, 12, 21, 10, 25), Duration.ofMinutes(10));
        taskManager.addTask(task);
        assertEquals(taskManager.getTask(0).getTitle(), task.getTitle());
        assertEquals(taskManager.getTask(0).getDescription(), task.getDescription());
        assertEquals(LocalDateTime.of(2022, 12, 21, 10, 25), task.getStartTime());
        assertEquals(Duration.ofMinutes(10), task.getDuration());
    }

    @Test
    public void testingAddEpic() {
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут",
                LocalDateTime.of(2022, 12, 21, 10, 25), Duration.ofMinutes(10));
        taskManager.addEpic(epic);
        assertEquals(taskManager.getEpic(0).getTitle(), epic.getTitle());
        assertEquals(taskManager.getEpic(0).getDescription(), epic.getDescription());
        assertEquals(taskManager.getEpic(0).getStatus(), TaskStatus.NEW);
        assertEquals(LocalDateTime.of(2022, 12, 21, 10, 25), epic.getStartTime());
        assertEquals(Duration.ofMinutes(10), epic.getDuration());
    }

    @Test
    public void testingAddSubTask() {
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут", LocalDateTime.of(2022, 12, 21, 10, 25), Duration.ofMinutes(10));
        SubTask subTask = new SubTask("Выйти на улицу", "Будем спортсменами и спустимся по лестнице",
                TaskStatus.NEW, 0, LocalDateTime.of(2022, 12, 21, 11, 25), Duration.ofMinutes(20));
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);
        assertEquals(taskManager.getSubTask(1).getTitle(), subTask.getTitle());
        assertEquals(taskManager.getSubTask(1).getDescription(), subTask.getDescription());
        assertEquals(taskManager.getSubTask(1).getStatus(), TaskStatus.NEW);
        assertEquals(LocalDateTime.of(2022, 12, 21, 11, 25), epic.getStartTime());
        assertEquals(Duration.ofMinutes(20), epic.getDuration());
    }

    @Test
    public void testingGetTask() {
        Task task = new Task("a", "b",
                LocalDateTime.of(2022, 12, 21, 11, 25), Duration.ofMinutes(15));
        Task task1 = new Task("c", "d",
                LocalDateTime.of(2022, 12, 21, 10, 25), Duration.ofMinutes(15));
        taskManager.addTask(task);
        taskManager.addTask(task1);
        Task task2 = taskManager.getTask(0);
        Task task3 = taskManager.getTask(1);
        assertEquals(task2.getTitle(), task.getTitle());
        assertEquals(task3.getTitle(), task1.getTitle());
        assertEquals(task2.getDescription(), task.getDescription());
        assertEquals(task3.getDescription(), task1.getDescription());
        assertEquals(task2.getStartTime(), task.getStartTime());
        assertEquals(task3.getStartTime(), task1.getStartTime());
        assertEquals(task2.getDuration(), task.getDuration());
        assertEquals(task3.getDuration(), task1.getDuration());
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        assertEquals(taskManager.getHistory().get(0).getId(), list.get(0));
        assertEquals(taskManager.getHistory().get(1).getId(), list.get(1));
        assertNull(taskManager.getTask(10));
    }

    @Test
    public void testingGetEpic() {
        Epic epic = new Epic("a", "b");
        Epic epic1 = new Epic("c", "d");
        taskManager.addEpic(epic);
        taskManager.addEpic(epic1);
        Epic epic2 = taskManager.getEpic(0);
        Epic epic3 = taskManager.getEpic(1);
        assertEquals(epic2.getTitle(), epic.getTitle());
        assertEquals(epic3.getTitle(), epic1.getTitle());
        assertEquals(epic2.getDescription(), epic.getDescription());
        assertEquals(epic3.getDescription(), epic1.getDescription());
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        assertEquals(taskManager.getHistory().get(0).getId(), list.get(0));
        assertEquals(taskManager.getHistory().get(1).getId(), list.get(1));
        assertNull(taskManager.getEpic(10));
    }

    @Test
    public void testingGetSubTask() {
        Epic epic = new Epic("a", "b");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("c", "d", 0);
        SubTask subTask1 = new SubTask("e", "f", 0);
        taskManager.addSubTask(subTask);
        taskManager.addSubTask(subTask1);
        SubTask subTask2 = taskManager.getSubTask(1);
        SubTask subTask3 = taskManager.getSubTask(2);
        assertEquals(subTask2.getTitle(), subTask.getTitle());
        assertEquals(subTask3.getTitle(), subTask1.getTitle());
        assertEquals(subTask2.getDescription(), subTask.getDescription());
        assertEquals(subTask3.getDescription(), subTask1.getDescription());
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        assertEquals(taskManager.getHistory().get(0).getId(), list.get(0));
        assertEquals(taskManager.getHistory().get(1).getId(), list.get(1));
        assertNull(taskManager.getSubTask(10));
    }

    @Test
    public void testingAddErrorSubTask() {
        SubTask subTask = new SubTask("Выйти на улицу", "Будем спортсменами и спустимся по лестнице", TaskStatus.NEW, 0);
        assertThrows(RuntimeException.class, () -> taskManager.addSubTask(subTask));
    }

    @Test
    public void testingGetSubTaskList() {
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут");
        taskManager.addEpic(epic);
        assertEquals(taskManager.getSubTaskList(0), new ArrayList<Integer>());
        SubTask subTask = new SubTask("Выйти на улицу", "Будем спортсменами и спустимся по лестнице", TaskStatus.NEW, 0);
        SubTask subTask1 = new SubTask("a", "b", TaskStatus.NEW, 0);
        taskManager.addSubTask(subTask);
        taskManager.addSubTask(subTask1);
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        assertEquals(taskManager.getSubTaskList(0), list);
    }

    @Test
    public void testingGetEpicMap() {
        List<Integer> list = new ArrayList<>();
        assertEquals(0, taskManager.getEpicsMap().size());
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут");
        Epic epic1 = new Epic("a", "b");
        taskManager.addEpic(epic);
        taskManager.addEpic(epic1);
        assertEquals(2, taskManager.getEpicsMap().size());
        assertEquals(epic.getTitle(), taskManager.getEpicsMap().get(0).getTitle());
        assertEquals(epic.getDescription(), taskManager.getEpicsMap().get(0).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getEpicsMap().get(0).getStatus());
        assertEquals(list, taskManager.getSubTaskList(0));
        assertEquals(epic1.getTitle(), taskManager.getEpicsMap().get(1).getTitle());
        assertEquals(epic1.getDescription(), taskManager.getEpicsMap().get(1).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getEpicsMap().get(1).getStatus());
    }

    @Test
    public void testingGetTaskMap() {
        assertEquals(0, taskManager.getTasksMap().size());
        Task task = new Task("Выгулять кота", "Гулять с ним 30 минут");
        Task task1 = new Task("a", "b");
        taskManager.addTask(task);
        taskManager.addTask(task1);
        assertEquals(2, taskManager.getTasksMap().size());
        assertEquals(task.getTitle(), taskManager.getTasksMap().get(0).getTitle());
        assertEquals(task.getDescription(), taskManager.getTasksMap().get(0).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getTasksMap().get(0).getStatus());
        assertEquals(task1.getTitle(), taskManager.getTasksMap().get(1).getTitle());
        assertEquals(task1.getDescription(), taskManager.getTasksMap().get(1).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getTasksMap().get(1).getStatus());
    }

    @Test
    public void testingGetSubTasksMap() {
        List<Integer> list = new ArrayList<>();
        assertEquals(0, taskManager.getEpicsMap().size());
        assertEquals(0, taskManager.getSubTasksMap().size());
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут");
        SubTask subTask = new SubTask("a", "b", 0);
        SubTask subTask1 = new SubTask("c", "d", 0);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);
        taskManager.addSubTask(subTask1);
        assertEquals(1, taskManager.getEpicsMap().size());
        assertEquals(2, taskManager.getSubTasksMap().size());
        assertEquals(epic.getTitle(), taskManager.getEpicsMap().get(0).getTitle());
        assertEquals(epic.getDescription(), taskManager.getEpicsMap().get(0).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getEpicsMap().get(0).getStatus());
        list.add(1);
        list.add(2);
        assertEquals(list, taskManager.getSubTaskList(0));
        assertEquals(subTask.getTitle(), taskManager.getSubTasksMap().get(1).getTitle());
        assertEquals(subTask.getDescription(), taskManager.getSubTasksMap().get(1).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getSubTasksMap().get(1).getStatus());
        assertEquals(subTask1.getTitle(), taskManager.getSubTasksMap().get(2).getTitle());
        assertEquals(subTask1.getDescription(), taskManager.getSubTasksMap().get(2).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getSubTasksMap().get(2).getStatus());
    }

    @Test
    public void testingClearTask() {
        assertEquals(0, taskManager.getTasksMap().size());
        Task task = new Task("Выгулять кота", "Гулять с ним 30 минут");
        Task task1 = new Task("a", "b");
        taskManager.addTask(task);
        taskManager.addTask(task1);
        assertEquals(2, taskManager.getTasksMap().size());
        taskManager.clearTask();
        assertEquals(0, taskManager.getTasksMap().size());
    }

    @Test
    public void testingClearSubTask() {
        List<Integer> list = new ArrayList<>();
        assertEquals(0, taskManager.getEpicsMap().size());
        assertEquals(0, taskManager.getSubTasksMap().size());
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут");
        SubTask subTask = new SubTask("a", "b", 0);
        SubTask subTask1 = new SubTask("c", "d", 0);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);
        taskManager.addSubTask(subTask1);
        assertEquals(1, taskManager.getEpicsMap().size());
        assertEquals(2, taskManager.getSubTasksMap().size());
        list.add(1);
        list.add(2);
        assertEquals(list, taskManager.getSubTaskList(0));
        taskManager.clearSubTask();
        assertEquals(0, taskManager.getSubTasksMap().size());
        assertNull(taskManager.getSubTaskList(0));
    }

    @Test
    public void testingClearEpic() {
        List<Integer> list = new ArrayList<>();
        assertEquals(0, taskManager.getEpicsMap().size());
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут");
        Epic epic1 = new Epic("a", "b");
        taskManager.addEpic(epic);
        taskManager.addEpic(epic1);
        assertEquals(2, taskManager.getEpicsMap().size());
        assertEquals(list, taskManager.getSubTaskList(0));
        taskManager.clearEpic();
        assertEquals(0, taskManager.getEpicsMap().size());
    }

    @Test
    public void testingRemoveTask() {
        assertEquals(0, taskManager.getTasksMap().size());
        Task task = new Task("Выгулять кота", "Гулять с ним 30 минут");
        taskManager.addTask(task);
        assertEquals(1, taskManager.getTasksMap().size());
        taskManager.removeTask(0);
        assertEquals(0, taskManager.getTasksMap().size());
    }

    @Test
    public void testingRemoveEpic() {
        assertEquals(0, taskManager.getEpicsMap().size());
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут");
        taskManager.addEpic(epic);
        assertEquals(1, taskManager.getEpicsMap().size());
        taskManager.removeEpic(0);
        assertEquals(0, taskManager.getEpicsMap().size());
    }

    @Test
    public void testingRemoveSubTask() {
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут");
        SubTask subTask = new SubTask("a", "b", TaskStatus.DONE, 0);
        taskManager.addEpic(epic);
        assertEquals(TaskStatus.NEW, taskManager.getEpic(0).getStatus());
        taskManager.addSubTask(subTask);
        assertEquals(TaskStatus.DONE, taskManager.getEpic(0).getStatus());
        assertEquals(1, taskManager.getSubTasksMap().size());
        taskManager.removeSubTask(1);
        assertEquals(0, taskManager.getSubTasksMap().size());
        assertEquals(TaskStatus.NEW, taskManager.getEpic(0).getStatus());
    }

    @Test
    public void testingUpdateTask() {
        assertEquals(0, taskManager.getTasksMap().size());
        Task task = new Task("Выгулять кота", "Гулять с ним 30 минут");
        Task task1 = new Task(1, "a", "b", TaskStatus.NEW);
        taskManager.addTask(task);
        taskManager.addTask(task);
        assertEquals(2, taskManager.getTasksMap().size());
        assertEquals(task.getTitle(), taskManager.getTasksMap().get(0).getTitle());
        assertEquals(task.getDescription(), taskManager.getTasksMap().get(0).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getTasksMap().get(0).getStatus());
        taskManager.updateTask(task1);
        assertEquals(task1.getTitle(), taskManager.getTasksMap().get(1).getTitle());
        assertEquals(task1.getDescription(), taskManager.getTasksMap().get(1).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getTasksMap().get(1).getStatus());
    }

    @Test
    public void testingUpdateSubTask() {
        assertEquals(0, taskManager.getEpicsMap().size());
        assertEquals(0, taskManager.getSubTasksMap().size());
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут");
        SubTask subTask = new SubTask("a", "b", 0);
        SubTask subTask1 = new SubTask(2, "c", "d", 0);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);
        taskManager.addSubTask(subTask);
        assertEquals(epic.getTitle(), taskManager.getEpicsMap().get(0).getTitle());
        assertEquals(epic.getDescription(), taskManager.getEpicsMap().get(0).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getEpicsMap().get(0).getStatus());
        assertEquals(subTask.getTitle(), taskManager.getSubTasksMap().get(1).getTitle());
        assertEquals(subTask.getDescription(), taskManager.getSubTasksMap().get(1).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getSubTasksMap().get(1).getStatus());
        taskManager.updateSubTask(subTask1);
        assertEquals(subTask1.getTitle(), taskManager.getSubTasksMap().get(2).getTitle());
        assertEquals(subTask1.getDescription(), taskManager.getSubTasksMap().get(2).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getSubTasksMap().get(2).getStatus());
    }

    @Test
    public void testingUpdateEpic() {
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут");
        Epic epic1 = new Epic(1, "a", "b", TaskStatus.NEW);
        taskManager.addEpic(epic);
        taskManager.addEpic(epic);
        assertEquals(2, taskManager.getEpicsMap().size());
        assertEquals(epic.getTitle(), taskManager.getEpicsMap().get(0).getTitle());
        assertEquals(epic.getDescription(), taskManager.getEpicsMap().get(0).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getEpicsMap().get(0).getStatus());
        taskManager.updateEpic(epic1);
        assertEquals(epic1.getTitle(), taskManager.getEpicsMap().get(1).getTitle());
        assertEquals(epic1.getDescription(), taskManager.getEpicsMap().get(1).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getEpicsMap().get(1).getStatus());
    }

    @Test
    public void testingHistoryManagerEmptyTaskHistory() {
        Task task = new Task("a", "b");
        Task task1 = new Task("f", "g");
        Epic epic = new Epic("c", "d");
        taskManager.addTask(task);
        taskManager.addTask(task1);
        taskManager.addEpic(epic);
        assertEquals(taskManager.getHistory().size(), 0);
        taskManager.getTask(0);
        taskManager.getTask(0);
        List<Integer> list = new ArrayList<>();
        list.add(0);
        assertEquals(list.get(0), taskManager.getHistory().get(0).getId());
        assertEquals(1, taskManager.getHistory().size());
        taskManager.getTask(1);
        taskManager.getEpic(2);
        list.add(1);
        list.add(2);
        assertEquals(3, taskManager.getHistory().size());
        assertEquals(list.get(0), taskManager.getHistory().get(0).getId());
        assertEquals(list.get(1), taskManager.getHistory().get(1).getId());
        assertEquals(list.get(2), taskManager.getHistory().get(2).getId());
        taskManager.removeTask(1);
        assertEquals(2, taskManager.getHistory().size());
        assertEquals(list.get(0), taskManager.getHistory().get(0).getId());
        assertEquals(list.get(2), taskManager.getHistory().get(1).getId());
        taskManager.addTask(task1);
        taskManager.getTask(3);
        taskManager.removeTask(0);
        list.add(3);
        assertEquals(2, taskManager.getHistory().size());
        assertEquals(list.get(2), taskManager.getHistory().get(0).getId());
        assertEquals(list.get(3), taskManager.getHistory().get(1).getId());
    }
}
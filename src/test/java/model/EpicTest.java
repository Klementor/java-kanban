package model;

import manager.Managers;
import manager.interfaces.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    File file = new File("testing.csv");

    @AfterEach
    public void deleteFile() {
        file.delete();
    }

    @Test
    public void testingForEpicEmptyListOfSubtasks() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут");
        taskManager.addEpic(epic);
        TaskStatus epicsStatus = taskManager.getEpic(0).getStatus();
        assertEquals(epicsStatus, TaskStatus.NEW);
    }

    @Test
    public void testingForEpicAllSubtasksWithNewStatus() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Выйти на улицу", "Будем спортсменами и спустимся по лестнице", TaskStatus.NEW, 0);
        SubTask subTask1 = new SubTask("a", "b", TaskStatus.NEW, 0);
        taskManager.addSubTask(subTask);
        taskManager.addSubTask(subTask1);
        TaskStatus epicsStatus = taskManager.getEpic(0).getStatus();
        assertEquals(epicsStatus, TaskStatus.NEW);
    }

    @Test
    public void testingForEpicAllSubtasksWithDoneStatus() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Выйти на улицу", "Будем спортсменами и спустимся по лестнице", TaskStatus.DONE, 0);
        SubTask subTask1 = new SubTask("a", "b", TaskStatus.DONE, 0);
        taskManager.addSubTask(subTask);
        taskManager.addSubTask(subTask1);
        TaskStatus epicsStatus = taskManager.getEpic(0).getStatus();
        assertEquals(epicsStatus, TaskStatus.DONE);
    }

    @Test
    public void testingForEpicSubtasksWithNewAndDoneStatuses() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Выйти на улицу", "Будем спортсменами и спустимся по лестнице", TaskStatus.DONE, 0);
        SubTask subTask1 = new SubTask("a", "b", TaskStatus.NEW, 0);
        taskManager.addSubTask(subTask);
        taskManager.addSubTask(subTask1);
        TaskStatus epicsStatus = taskManager.getEpic(0).getStatus();
        assertEquals(epicsStatus, TaskStatus.IN_PROGRESS);
    }

    @Test
    public void testingForEpicSubtasksWithStatusInProgress() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Выйти на улицу", "Будем спортсменами и спустимся по лестнице", TaskStatus.IN_PROGRESS, 0);
        SubTask subTask1 = new SubTask("a", "b", TaskStatus.IN_PROGRESS, 0);
        taskManager.addSubTask(subTask);
        taskManager.addSubTask(subTask1);
        TaskStatus epicsStatus = taskManager.getEpic(0).getStatus();
        assertEquals(epicsStatus, TaskStatus.IN_PROGRESS);
    }
}

import Manager.Managers;
import Manager.interfaces.TaskManager;
import model.*;

public class Test {
    TaskManager taskManager = Managers.getDefault();

    public void testing() {
        Task task1 = new Task("Закупить продукты", "");
        taskManager.addTask(task1);

        Task task2 = new Task("Позвонить в банк", "");
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Погладить кота", "Просто погладить кота");
        taskManager.addEpic(epic1);

        SubTask subtask1 = new SubTask("Найти кота", "Найти среди хаоса кота", epic1.getId());
        taskManager.addSubTask(subtask1);

        SubTask subtask2 = new SubTask("Поймать кота", "Желания кота и наши не сошлись", epic1.getId());
        taskManager.addSubTask(subtask2);

        SubTask subtask3 = new SubTask("Погладить кота", "Погладить несчастного кота", epic1.getId());
        taskManager.addSubTask(subtask3);

        Epic epic2 = new Epic("Посмотреть сериал", "");
        taskManager.addEpic(epic2);

        taskManager.getTask(task1.getId());
        System.out.println("[Запрос] task1 -> id " + task1.getId());
        taskManager.getTask(task2.getId());
        System.out.println("[Запрос] task2 -> id " + task2.getId());
        System.out.print("[История] ");
        System.out.println(taskManager.getHistory());
        System.out.println();
        taskManager.getTask(task1.getId());
        System.out.println("[Запрос] task1 -> id " + task1.getId());
        taskManager.getTask(task1.getId());
        System.out.println("[Запрос] task1 -> id " + task1.getId());
        System.out.print("[История] ");
        System.out.println(taskManager.getHistory());
        System.out.println();
        taskManager.getSubTask(subtask1.getId());
        System.out.println("[Запрос] subtask1 -> id " + subtask1.getId());
        taskManager.getSubTask(subtask3.getId());
        System.out.println("[Запрос] subtask3 -> id " + subtask3.getId());
        taskManager.getSubTask(subtask2.getId());
        System.out.println("[Запрос] subtask2 -> id " + subtask2.getId());
        System.out.print("[История] ");
        System.out.println(taskManager.getHistory());
        System.out.println();
        taskManager.getEpic(epic1.getId());
        System.out.println("[Запрос] epic1 -> id " + epic1.getId());
        System.out.print("[История] ");
        System.out.println(taskManager.getHistory());
        System.out.println();
        taskManager.removeTask(task1.getId());
        System.out.println("[Запрос] удаление task1 -> id " + task1.getId());
        System.out.print("[История] ");
        System.out.println(taskManager.getHistory());
        System.out.println();
        taskManager.removeEpic(epic1.getId());
        System.out.println("[Запрос] удаление epic1 -> id " + epic1.getId());
        System.out.print("[История] ");
        System.out.println(taskManager.getHistory());
    }
}
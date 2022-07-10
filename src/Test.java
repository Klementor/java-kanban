import manager.Managers;
import manager.interfaces.TaskManager;
import model.Task;
import model.TaskStatus;

public class Test {
    TaskManager taskManager = Managers.getDefault();

    public void testing() {
        taskManager.addTask(new Task("Выгулять кота", "Выгуливать его 40 минут", TaskStatus.NEW));
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getTask(1));
        taskManager.getTask(0);
    }
}
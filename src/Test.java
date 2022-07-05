import Manager.Managers;
import Manager.interfaces.TaskManager;

public class Test {
    TaskManager taskManager = Managers.getDefault();

    public void testing() {
        //taskManager.addTask(new Task("Выгулять кота", "Выгуливать его 40 минут", TaskStatus.NEW));
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getTask(1));
        taskManager.getTask(0);
    }
}
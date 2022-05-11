import InMemoryTaskManager.implementation.InMemoryHistoryManager;
import InMemoryTaskManager.implementation.InMemoryTaskManager;
import InMemoryTaskManager.Managers;
import model.*;

public class Test {
    InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();

    public void testing() {
        Task task1 = new Task("Сходить за продуктами", "");
        inMemoryTaskManager.addTask(task1);
        Task task2 = new Task("Позвонить родителям", "");
        inMemoryTaskManager.addTask(task2);

        Epic epic1 = new Epic("Поиграть с сестрой", "Поиграть во дворе");
        inMemoryTaskManager.addEpic(epic1);
        SubTask subtask1 = new SubTask("Позвать сестру домой",
                "Найти сестру среди других детей", epic1.getId());
        inMemoryTaskManager.addSubTask(subtask1);
        SubTask subtask2 = new SubTask("Пригрозить, что расскажу родителям",
                "Желания сестры и мои не сошлись", epic1.getId());
        inMemoryTaskManager.addSubTask(subtask2);

        Epic epic2 = new Epic("Посмотреть фильм с девушкой", "");
        inMemoryTaskManager.addEpic(epic2);
        SubTask subtask3 = new SubTask("Подготовить вкусняшки", "", epic2.getId());
        inMemoryTaskManager.addSubTask(subtask3);

        printLists();

//        printDescription();
//
//        task1.setStatus(TaskStatus.IN_PROGRESS);
//        inMemoryTaskManager.updateTask(task1);
//
//        printStatus(task1);
//
//        task2.setStatus(TaskStatus.DONE);
//        inMemoryTaskManager.updateTask(task2);
//
//        printStatus(task2);
//
//        subtask1.setStatus(TaskStatus.IN_PROGRESS);
//        inMemoryTaskManager.updateSubTask(subtask1);
//
//        printStatus(epic1);
//
//        subtask3.setStatus(TaskStatus.DONE);
//        inMemoryTaskManager.updateSubTask(subtask3);
//
//        printStatus(epic2);
//
//        inMemoryTaskManager.clearSubTask();

//        printLists();

        inMemoryTaskManager.getTask(1);
        inMemoryTaskManager.getTask(0);
        inMemoryTaskManager.getSubTask(3);
        inMemoryTaskManager.getSubTask(4);
        inMemoryTaskManager.getSubTask(6);
        inMemoryTaskManager.getEpic(2);
        inMemoryTaskManager.getEpic(5);
        inMemoryTaskManager.getTask(1);
        inMemoryTaskManager.getTask(0);
        inMemoryTaskManager.getSubTask(3);
        getHistory();
        inMemoryTaskManager.getTask(2); // если обращаться к задаче с неправильным id, в историю запросов
        inMemoryTaskManager.getTask(2); // это попадать не будет
        inMemoryTaskManager.getTask(2);
        getHistory();
    }

    public void printStatus(Task task) {
        System.out.print("Task\t[" + task.getId() + "] \t");
        System.out.println(inMemoryTaskManager.getTask(task.getId()).getStatus());
    }

    public void printStatus(SubTask subtask) {
        System.out.print("Subtask\t[" + subtask.getEpicId() + "][" + subtask.getId() + "] \t");
        System.out.println(inMemoryTaskManager.getSubTask(subtask.getId()).getStatus());
    }

    public void printStatus(Epic epic) {
        System.out.print("Epic\t[" + epic.getId() + "][" + epic.getIdSubTasks().size() + "] \t");
        System.out.println(inMemoryTaskManager.getEpic(epic.getId()).getStatus());
        for (int id : epic.getIdSubTasks()) {
            printStatus(inMemoryTaskManager.getSubTask(id));
        }
    }

    public void printDescription() {
        System.out.println();
        System.out.println("Task[ID задачи]");
        System.out.println("Epic[ID Эпика][Кол-во подзадач]");
        System.out.println("Subtask[ID Эпика][ID подзадачи]");
    }

    private void printLists() {
        System.out.println();
        System.out.println(inMemoryTaskManager.getTasksMap());
        System.out.println(inMemoryTaskManager.getEpicsMap());
        System.out.println(inMemoryTaskManager.getSubTasksMap());
    }

    private void getHistory() {
        System.out.println();
        System.out.println("История запросов");
        System.out.println(inMemoryTaskManager.getHistory());
    }
}

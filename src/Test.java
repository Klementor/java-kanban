import model.*;
import manager.Manager;

public class Test {
    Manager manager = new Manager();

    public void testing() {
        Task task1 = new Task("Сходить за продуктами", "");
        manager.addTask(task1);
        Task task2 = new Task("Позвонить родителям", "");
        manager.addTask(task2);

        Epic epic1 = new Epic("Поиграть с сестрой", "Поиграть во дворе");
        manager.addEpic(epic1);
        SubTask subtask1 = new SubTask("Позвать сестру домой",
                "Найти сестру среди других детей", epic1.getId());
        manager.addSubTask(subtask1);
        SubTask subtask2 = new SubTask("Пригрозить, что расскажу родителям",
                "Желания сестры и мои не сошлись", epic1.getId());
        manager.addSubTask(subtask2);

        Epic epic2 = new Epic("Посмотреть фильм с девушкой", "");
        manager.addEpic(epic2);
        SubTask subtask3 = new SubTask("Подготовить вкусняшки", "", epic2.getId());
        manager.addSubTask(subtask3);


        printLists();

        printDescription();

        task1.setStatus(String.valueOf(TaskStatus.IN_PROGRESS));
        manager.updateTask(task1);

        printStatus(task1);

        task2.setStatus(String.valueOf(TaskStatus.DONE));
        manager.updateTask(task2);

        printStatus(task2);

        subtask1.setStatus(String.valueOf(TaskStatus.IN_PROGRESS));
        manager.updateSubTask(subtask1);

        printStatus(epic1);

        subtask3.setStatus(String.valueOf(TaskStatus.DONE));
        manager.updateSubTask(subtask3);

        printStatus(epic2);

        manager.clearSubTask();

        printLists();
    }

    public void printStatus(Task task) {
        System.out.print("Task\t[" + task.getId() + "] \t");
        System.out.println(manager.getTask(task.getId()).getStatus());
    }

    public void printStatus(SubTask subtask) {
        System.out.print("Subtask\t[" + subtask.getEpicId() + "][" + subtask.getId() + "] \t");
        System.out.println(manager.getSubTask(subtask.getId()).getStatus());
    }

    public void printStatus(Epic epic) {
        System.out.print("Epic\t[" + epic.getId() + "][" + epic.getIdSubTasks().size() + "] \t");
        System.out.println(manager.getEpic(epic.getId()).getStatus());
        for (int id : epic.getIdSubTasks()) {
            printStatus(manager.getSubTask(id));
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
        System.out.println(manager.getTaskHashMap());
        System.out.println(manager.getEpicHashMap());
        System.out.println(manager.getSubTaskHashMap());
    }
}

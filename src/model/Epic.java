package model;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> idSubTasks;

    public Epic(String title, String description, String status) {
        super(title, description, status);
        idSubTasks = new ArrayList<>();
    }

    public Epic(String title, String description) {
        super(title, description, String.valueOf(TaskStatus.NEW));
        idSubTasks = new ArrayList<>();
    }

    public ArrayList<Integer> getIdSubTasks() {
        return idSubTasks;
    }

    public void setIdSubTasks(ArrayList<Integer> idSubTasks) {
        this.idSubTasks = idSubTasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return Objects.equals(idSubTasks, epic.idSubTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSubTasks);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "idSubTasks=" + idSubTasks +
                '}';
    }
}
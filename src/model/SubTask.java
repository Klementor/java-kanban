package model;

import java.util.Objects;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String title, String description, TaskStatus taskStatus, int epicId) {
        super(title, description, taskStatus);
        this.epicId = epicId;
    }

    public SubTask(String title, String description, int epicId) {
        super(title, description, TaskStatus.NEW);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubTask subTask = (SubTask) o;
        return epicId == subTask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(epicId);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                '}';
    }
}

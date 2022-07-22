package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class Epic extends Task {
    private ArrayList<Integer> idSubTasks = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String title, String description, TaskStatus taskStatus) {
        super(title, description, taskStatus);
    }

    public Epic(String title, String description) {
        super(title, description, TaskStatus.NEW);
    }

    public Epic(int id, String title, String description, TaskStatus taskStatus) {
        super(id, title, description, taskStatus);
    }

    public Epic(String title, String description, TaskStatus taskStatus, LocalDateTime startTime, Duration duration) {
        super(title, description, taskStatus, startTime, duration);
    }

    public Epic(String title, String description, LocalDateTime startTime, Duration duration) {
        super(title, description, TaskStatus.NEW, startTime, duration);
    }

    public Epic(int id, String title, String description, TaskStatus taskStatus, LocalDateTime startTime, Duration duration) {
        super(id, title, description, taskStatus, startTime, duration);
    }

    public ArrayList<Integer> getIdSubTasks() {
        return idSubTasks;
    }

    public void setIdSubTasks(ArrayList<Integer> idSubTasks) {
        this.idSubTasks = idSubTasks;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
    }

    @Override
    public Optional<LocalDateTime> getEndTime() {
        if (endTime == null) {
            return Optional.empty();
        } else {
            return Optional.of(endTime);
        }
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "idSubTasks=" + idSubTasks +
                ", endTime=" + endTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(idSubTasks, epic.idSubTasks) && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idSubTasks, endTime);
    }
}

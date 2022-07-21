package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class Task {
    private String title;
    private String description;
    private int id;
    private TaskStatus status;
    private LocalDateTime startTime;
    private Duration duration;

    public Task(String title, String description, TaskStatus taskStatus) {
        this.title = title;
        this.description = description;
        this.status = taskStatus;
    }

    public Task(String title, String description, TaskStatus taskStatus, LocalDateTime startTime, Duration duration) {
        this.title = title;
        this.description = description;
        this.status = taskStatus;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = TaskStatus.NEW;
    }

    public Task(String title, String description, LocalDateTime startTime, Duration duration) {
        this.title = title;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(int id, String title, String description, TaskStatus taskStatus){
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = taskStatus;
    }

    public Task(int id, String title, String description, TaskStatus taskStatus, LocalDateTime startTime, Duration duration){
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = taskStatus;
        this.startTime = startTime;
        this.duration = duration;
    }

    public TaskType getTaskType() {
        return TaskType.TASK;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Optional<LocalDateTime> getEndTime(){
        if (startTime == null|| duration == null){
            return Optional.empty();
        }
        return Optional.of(startTime.plus(duration));
    }


    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(title, task.title) && Objects.equals(description, task.description)
                && status == task.status && Objects.equals(startTime, task.startTime)
                && Objects.equals(duration, task.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, id, status, startTime, duration);
    }
}
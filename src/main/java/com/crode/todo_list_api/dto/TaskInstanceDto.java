package com.crode.todo_list_api.dto;

import com.crode.todo_list_api.utils.TaskType;
import java.time.LocalDateTime;

public class TaskInstanceDto {

    private Long id;
    private Long taskId;
    private LocalDateTime date;
    private Boolean completed;
    private TaskType type;

    private String title;

    private String description;

    public TaskInstanceDto() {};

    public TaskInstanceDto(Long id, Long taskId, LocalDateTime date, Boolean completed, TaskType type, String title, String description) {
        this.id = id;
        this.taskId = taskId;
        this.date = date;
        this.completed = completed;
        this.type = type;
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

package com.crode.todo_list_api.utils;

import com.crode.todo_list_api.dto.TaskDto;
import com.crode.todo_list_api.model.Task;

public class TaskUtil {

    public static TaskDto taskToDto(Task task) {
        TaskDto dto = new TaskDto();
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStartDate(task.getStartDate());
        dto.setEndDate(task.getEndDate());
        dto.setDueDate(task.getDueDate());
        return dto;
    }
}

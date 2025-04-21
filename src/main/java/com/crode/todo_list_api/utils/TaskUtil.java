package com.crode.todo_list_api.utils;

import com.crode.todo_list_api.dto.TaskDto;
import com.crode.todo_list_api.dto.TaskInstanceDto;
import com.crode.todo_list_api.model.Task;
import com.crode.todo_list_api.model.TaskInstance;

public class TaskUtil {

    public static TaskDto taskToDto(Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setType(task.getType().name());
        dto.setStartDate(task.getStartDate());
        dto.setEndDate(task.getEndDate());
        dto.setDueDate(task.getDueDate());
        return dto;
    }

    public static TaskInstanceDto taskInstanceToDto(TaskInstance taskInstance) {
        return new TaskInstanceDto(
            taskInstance.getId(),
            taskInstance.getTask().getId(),
            taskInstance.getDate(),
            taskInstance.isCompleted(),
            taskInstance.getTask().getType(),
            taskInstance.getTask().getTitle(),
            taskInstance.getTask().getDescription()
        );
    }
}

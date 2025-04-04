package com.crode.todo_list_api.service;

import com.crode.todo_list_api.dto.TaskDto;
import com.crode.todo_list_api.model.Task;
import java.util.List;

public interface TaskService {

    Task createTask(TaskDto dto);
    Task updateTask(Long id, TaskDto dto);
    void deleteTask(Long id);
    Task getTaskById(Long id);
    List<Task> getAllTasks();
    List<Task> getTasksByCompletionStatus(boolean completed);
}

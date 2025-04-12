package com.crode.todo_list_api.service;

import com.crode.todo_list_api.model.TaskInstance;
import java.time.LocalDateTime;
import java.util.List;

public interface TaskInstanceService {

    TaskInstance getTaskInstanceForTaskAndDate(Long taskId, LocalDateTime date);

    TaskInstance saveTaskInstance(TaskInstance taskInstance);

    TaskInstance getTaskInstanceForTaskAndDate(Long taskId, LocalDateTime start, LocalDateTime end);

    long getCompletedByTaskId(Long taskId);

    TaskInstance getById(long taskId);

    void updateTaskInstance(Long id, TaskInstance taskInstance);

    List<TaskInstance> getAll();
}

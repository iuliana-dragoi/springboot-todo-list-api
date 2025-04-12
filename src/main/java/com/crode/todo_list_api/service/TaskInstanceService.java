package com.crode.todo_list_api.service;

import com.crode.todo_list_api.model.TaskInstance;
import java.time.LocalDateTime;

public interface TaskInstanceService {

    TaskInstance getTaskInstanceForTaskAndDate(Long taskId, LocalDateTime date);

    TaskInstance saveTaskInstance(TaskInstance taskInstance);

    TaskInstance getTaskInstanceForTaskAndDate(Long taskId, LocalDateTime start, LocalDateTime end);
}

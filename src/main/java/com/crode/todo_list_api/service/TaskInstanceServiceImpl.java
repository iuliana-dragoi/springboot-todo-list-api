package com.crode.todo_list_api.service;

import com.crode.todo_list_api.model.TaskInstance;
import com.crode.todo_list_api.repository.TaskInstanceRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TaskInstanceServiceImpl implements TaskInstanceService{

    private final TaskInstanceRepository taskInstanceRepository;

    public TaskInstanceServiceImpl(TaskInstanceRepository taskInstanceRepository) {
        this.taskInstanceRepository = taskInstanceRepository;
    }

    @Override
    public TaskInstance getTaskInstanceForTaskAndDate(Long taskId, LocalDateTime date) {
        Optional<TaskInstance> taskInstance = taskInstanceRepository.findByTaskIdAndDate(taskId, date);
        return taskInstance.orElse(null);
    }

    @Override
    public TaskInstance saveTaskInstance(TaskInstance taskInstance) {
        return taskInstanceRepository.save(taskInstance);
    }

    @Override
    public TaskInstance getTaskInstanceForTaskAndDate(Long taskId, LocalDateTime start, LocalDateTime end) {
        return taskInstanceRepository.findByTaskIdAndDateRange(taskId, start, end).orElse(null);
    }

    @Override
    public long getCompletedByTaskId(Long taskId) {
        return taskInstanceRepository.countCompletedByTaskId(taskId);
    }

    @Override
    public TaskInstance getById(long taskId) {
        return taskInstanceRepository.findById(taskId).orElse(null);
    }

    @Override
    public void updateTaskInstance(Long id, TaskInstance taskInstance) {
        taskInstanceRepository.save(taskInstance);
    }
}

package com.crode.todo_list_api.service;

import com.crode.todo_list_api.dto.TaskDto;
import com.crode.todo_list_api.model.Task;
import com.crode.todo_list_api.model.TaskInstance;
import com.crode.todo_list_api.repository.TaskInstanceRepository;
import com.crode.todo_list_api.repository.TaskRepository;
import com.crode.todo_list_api.utils.TaskType;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;

    private final TaskInstanceRepository taskInstanceRepository;

    public TaskServiceImpl(TaskRepository repository, TaskInstanceRepository taskInstanceRepository) {
        this.repository = repository;
        this.taskInstanceRepository = taskInstanceRepository;
    }

    @Override
    public Task createTask(TaskDto dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStartDate(dto.getStartDate());
        task.setEndDate(dto.getEndDate());
        task.setDueDate(dto.getDueDate());
        TaskType type = TaskType.fromString(dto.getType());
        task.setType(type);
        task.setRecurrenceDays(dto.getRecurrenceDays());
        task = repository.save(task);

        if (task.getType() == TaskType.HABIT) {
            LocalDateTime currentDate = task.getStartDate();
            while (!currentDate.isAfter(task.getEndDate())) {
                createTaskInstance(task, currentDate);
                currentDate = currentDate.plusDays(1);
            }
        } else if (task.getType() == TaskType.ONE_TIME) {
            createTaskInstance(task, dto.getStartDate());
        }

        return task;
    }

    @Override
    public Task updateTask(Long id, TaskDto dto) {
        Optional<Task> optional = repository.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException("Task not found");
        }
        Task task = optional.get();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStartDate(dto.getStartDate());
        task.setEndDate(dto.getEndDate());
        task.setDueDate(dto.getDueDate());
        TaskType type = TaskType.fromString(dto.getType());
        task.setType(type);

        Task updatedTask = repository.save(task);

        taskInstanceRepository.deleteOutsideDateRange(id, dto.getStartDate(), dto.getEndDate());

        if (task.getType() == TaskType.HABIT) {
            LocalDateTime currentDate = dto.getStartDate();
            while (!currentDate.isAfter(dto.getEndDate())) {
                boolean exists = taskInstanceRepository.findByTaskIdAndDate(id, currentDate).isPresent();
                if (!exists) {
                    createTaskInstance(updatedTask, currentDate);
                }
                currentDate = currentDate.plusDays(1);
            }
        } else if (type == TaskType.ONE_TIME) {
            boolean exists = taskInstanceRepository.findByTaskIdAndDate(id, dto.getStartDate()).isPresent();
            if (!exists) {
                createTaskInstance(updatedTask, dto.getStartDate());
            }
        }

        return updatedTask;
    }

    private void createTaskInstance(Task task, LocalDateTime date) {
        TaskInstance instance = new TaskInstance();
        instance.setTask(task);
        instance.setDate(date);
        instance.setCompleted(false);
        taskInstanceRepository.save(instance);
    }

    @Override
    public void deleteTask(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Task getTaskById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Override
    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    @Override
    public List<Task> getTasksByCompletionStatus(boolean completed) {
        return repository.findByCompleted(completed);
    }
}

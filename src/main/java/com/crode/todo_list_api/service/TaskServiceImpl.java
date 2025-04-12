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
        task.setType(TaskType.HABIT);
        task.setRecurrenceDays(dto.getRecurrenceDays());
        task = repository.save(task);

        if (task.getType() == TaskType.HABIT) {
            LocalDateTime currentDate = task.getStartDate();
            while (!currentDate.isAfter(task.getEndDate())) {
                TaskInstance taskInstance = new TaskInstance();
                taskInstance.setTask(task);
                taskInstance.setDate(currentDate);
                taskInstance.setCompleted(false);
                taskInstanceRepository.save(taskInstance);
                currentDate = currentDate.plusDays(1);
            }
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

        Task updatedTask = repository.save(task);

        taskInstanceRepository.deleteOutsideDateRange(id, dto.getStartDate(), dto.getEndDate());
        LocalDateTime currentDate = dto.getStartDate();
        while (!currentDate.isAfter(dto.getEndDate())) {
            boolean exists = taskInstanceRepository.findByTaskIdAndDate(id, currentDate).isPresent();
            if (!exists) {
                TaskInstance taskInstance = new TaskInstance();
                taskInstance.setTask(updatedTask);
                taskInstance.setDate(currentDate);
                taskInstance.setCompleted(false);
                taskInstanceRepository.save(taskInstance);
            }
            currentDate = currentDate.plusDays(1);
        }

        return updatedTask;
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

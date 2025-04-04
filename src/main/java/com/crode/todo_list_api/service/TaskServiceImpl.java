package com.crode.todo_list_api.service;

import com.crode.todo_list_api.dto.TaskDto;
import com.crode.todo_list_api.model.Task;
import com.crode.todo_list_api.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;

    public TaskServiceImpl(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public Task createTask(TaskDto dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        return repository.save(task);
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
        task.setDueDate(dto.getDueDate());
        return repository.save(task);
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

package com.crode.todo_list_api.controller;

import com.crode.todo_list_api.dto.TaskDto;
import com.crode.todo_list_api.model.Task;
import com.crode.todo_list_api.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public String listTasks(Model model, @RequestParam(required = false) Boolean completed) {
        List<Task> tasks = (completed == null)
                ? service.getAllTasks()
                : service.getTasksByCompletionStatus(completed);
        model.addAttribute("tasks", tasks);
        return "task-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("task", new TaskDto());
        return "task-form";
    }

    @PostMapping
    public String createTask(@ModelAttribute TaskDto dto) {
        service.createTask(dto);
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Task task = service.getTaskById(id);
        TaskDto dto = new TaskDto();
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStartDate(task.getStartDate());
        dto.setEndDate(task.getEndDate());
        dto.setDueDate(task.getDueDate());
        model.addAttribute("taskId", id);
        model.addAttribute("task", dto);

        return "task-edit-form";
    }

    @PostMapping("/update/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute TaskDto dto) {
        service.updateTask(id, dto);
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        service.deleteTask(id);
        return "redirect:/tasks";
    }

    @GetMapping("/toggle/{id}")
    public String toggleCompletion(@PathVariable Long id) {
        Task task = service.getTaskById(id);
        task.setCompleted(!task.isCompleted());
        service.updateTask(id, taskToDto(task));
        return "redirect:/tasks";
    }

    private TaskDto taskToDto(Task task) {
        TaskDto dto = new TaskDto();
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStartDate(task.getStartDate());
        dto.setEndDate(task.getEndDate());
        dto.setDueDate(task.getDueDate());
        return dto;
    }
}


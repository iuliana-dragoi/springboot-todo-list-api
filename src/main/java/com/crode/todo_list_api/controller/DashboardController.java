package com.crode.todo_list_api.controller;

import com.crode.todo_list_api.dto.TaskInstanceDto;
import com.crode.todo_list_api.model.Task;
import com.crode.todo_list_api.service.TaskInstanceService;
import com.crode.todo_list_api.service.TaskService;
import com.crode.todo_list_api.utils.TaskType;
import com.crode.todo_list_api.utils.TaskUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final TaskService service;

    private final TaskInstanceService taskInstanceService;

    public DashboardController(TaskService service, TaskInstanceService taskInstanceService) {
        this.service = service;
        this.taskInstanceService = taskInstanceService;
    }

    @GetMapping()
    public String showDashboard(Model model) {
        List<Task> allTasks = service.getAllTasks();
        List<TaskInstanceDto> taskInstanceDtos = getTaskInstancesForToday(allTasks);
        model.addAttribute("tasks", taskInstanceDtos);
        return "dashboard/index";
    }

    private List<TaskInstanceDto> getTaskInstancesForToday(List<Task> tasks) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        return tasks.stream()
            .filter(task -> task.getType() == TaskType.HABIT)
            .map(task -> taskInstanceService.getTaskInstanceForTaskAndDate(task.getId(), startOfDay, endOfDay))
            .filter(Objects::nonNull)
            .map(TaskUtil::taskInstanceToDto)
            .toList();
    }
}

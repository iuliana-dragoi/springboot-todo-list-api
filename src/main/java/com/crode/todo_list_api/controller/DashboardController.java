package com.crode.todo_list_api.controller;

import com.crode.todo_list_api.dto.TaskDto;
import com.crode.todo_list_api.model.Task;
import com.crode.todo_list_api.service.TaskService;
import com.crode.todo_list_api.utils.TaskUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final TaskService service;

    public DashboardController(TaskService service) {
        this.service = service;
    }

    @GetMapping()
    public String showDashboard(Model model) {
        List<Task> allTasks = service.getAllTasks();
        List<TaskDto> dtoTasks = allTasks.stream().map(TaskUtil::taskToDto).toList();
        List<TaskDto> todayTasks = getTasksForToday(dtoTasks);
        model.addAttribute("tasks", todayTasks);
        return "dashboard/index";
    }

    private List<TaskDto> getTasksForToday(List<TaskDto> allTasks) {
        LocalDate today = LocalDate.now();
        return allTasks.stream().filter(
                task -> task.getStartDate() != null &&
                task.getEndDate() != null &&
                !task.getStartDate().toLocalDate().isAfter(today) &&
                !task.getEndDate().toLocalDate().isBefore(today)
        ).collect(Collectors.toList());
//        LocalDate today = LocalDate.now();
//        return taskRepository.findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(today, today); todo
    }
}

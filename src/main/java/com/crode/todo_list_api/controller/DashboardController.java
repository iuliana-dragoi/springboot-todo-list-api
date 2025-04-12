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
import java.util.*;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final TaskService taskService;

    private final TaskInstanceService taskInstanceService;

    public DashboardController(TaskService taskService, TaskInstanceService taskInstanceService) {
        this.taskService = taskService;
        this.taskInstanceService = taskInstanceService;
    }

    @GetMapping()
    public String showDashboard(Model model) {
        List<Task> tasks = taskService.getAllTasks();
        addTaskInstances(model, tasks);
        addTaskStatuses(model, tasks);
        return "dashboard/index";
    }

    private void addTaskInstances(Model model, List<Task> tasks) {
        List<TaskInstanceDto> taskInstanceDtos = getTaskInstancesForToday(tasks);

        List<TaskInstanceDto> completedTasks = new ArrayList<>();
        List<TaskInstanceDto> incompleteTasks = new ArrayList<>();
        for (TaskInstanceDto taskInstanceDto : taskInstanceDtos) {
            if (taskInstanceDto.getCompleted()) {
                completedTasks.add(taskInstanceDto);
            } else {
                incompleteTasks.add(taskInstanceDto);
            }
        }

        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("incompleteTasks", incompleteTasks);
//        model.addAttribute("tasks", taskInstanceDtos);
    }

    private void addTaskStatuses(Model model, List<Task> tasks) {
        Map<String, Long> completedMap = new HashMap<>();
        tasks.stream().forEach(task -> {
            long completed = taskInstanceService.getCompletedByTaskId(task.getId());
            completedMap.put(task.getTitle(), completed);
        });
        model.addAttribute("completedMap", completedMap);
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

package com.crode.todo_list_api.controller;

import com.crode.todo_list_api.dto.TaskInstanceDto;
import com.crode.todo_list_api.model.Task;
import com.crode.todo_list_api.model.TaskInstance;
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
import java.util.concurrent.atomic.AtomicInteger;

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
    }
    private void addTaskStatuses(Model model, List<Task> tasks) {
        Map<String, Long> completedMap = new HashMap<>();
        AtomicInteger oneTimeCount = new AtomicInteger();
        tasks.stream().forEach(task -> {
            long completed = taskInstanceService.getCompletedByTaskId(task.getId());

            if (task.getType() == TaskType.HABIT) {
                completedMap.put(task.getTitle(), completed);
            } else if (task.getType() == TaskType.ONE_TIME && completed > 0) {
                oneTimeCount.getAndIncrement();
            }
        });
        model.addAttribute("completedMap", completedMap);
        model.addAttribute("oneTimeTaskCount", oneTimeCount.get());
    }
    private List<TaskInstanceDto> getTaskInstancesForToday(List<Task> tasks) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        return tasks.stream()
            .map(task -> {
                List<TaskInstance> instances = taskInstanceService.getTaskInstanceForTaskAndDate(task.getId(), startOfDay, endOfDay);
                return instances.isEmpty() ? null : instances.get(0);
            })
            .filter(Objects::nonNull)
            .map(TaskUtil::taskInstanceToDto)
            .toList();
    }
}

package com.crode.todo_list_api.controller;

import com.crode.todo_list_api.dto.TaskInstanceDto;
import com.crode.todo_list_api.model.Task;
import com.crode.todo_list_api.model.TaskInstance;
import com.crode.todo_list_api.service.TaskInstanceService;
import com.crode.todo_list_api.utils.TaskUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard/task-instances")
public class TaskInstanceController {

    private final TaskInstanceService taskInstanceService;

    public TaskInstanceController(TaskInstanceService taskInstanceService) {
        this.taskInstanceService = taskInstanceService;
    }

    @GetMapping("/toggle/{id}")
    public String toggleCompletion(@PathVariable Long id) {
        TaskInstance taskInstance = taskInstanceService.getById(id);
        taskInstance.setCompleted(!taskInstance.isCompleted());
        taskInstanceService.updateTaskInstance(id, taskInstance);
        return "redirect:/dashboard";
    }

    @GetMapping("/details/{id}")
    public String viewTaskInstanceDetails(@PathVariable Long id, Model model) {
        TaskInstance clickedInstance = taskInstanceService.getById(id);
        if(clickedInstance == null) throw new RuntimeException("Task instance not found");

        Task task = clickedInstance.getTask();

        List<TaskInstanceDto> taskInstanceDtos = taskInstanceService
                .getTaskInstanceForTaskAndDate(task.getId(), task.getStartDate(), task.getEndDate())
                .stream()
                .map(TaskUtil::taskInstanceToDto)
                .collect(Collectors.toList());

        List<TaskInstanceDto> sortedTaskInstances = taskInstanceDtos.stream()
                .collect(Collectors.groupingBy(TaskInstanceDto::getFormattedDate, TreeMap::new, Collectors.toList()))
                .values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

//        List<TaskInstance> taskInstancesInPeriod = taskInstanceService.getTaskInstanceForTaskAndDate(task.getId(), task.getStartDate(), task.getEndDate());
//        List<TaskInstanceDto> taskInstancesDtos = taskInstancesInPeriod.stream().map(TaskUtil::taskInstanceToDto).toList();
//
//        TreeMap<LocalDateTime, List<TaskInstanceDto>> groupedByDate = new TreeMap<>();
//        for (TaskInstanceDto instance : taskInstancesDtos) {
//            groupedByDate.computeIfAbsent(instance.getDate(), k -> new ArrayList<>()).add(instance);
//        }
//
//        List<TaskInstanceDto> taskInstances = groupedByDate.values()
//            .stream()
//            .flatMap(List::stream)
//            .collect(Collectors.toList());
        
        model.addAttribute("taskInstances", sortedTaskInstances);
        model.addAttribute("task", clickedInstance.getTask());
        model.addAttribute("weekStart", task.getStartDate());
        model.addAttribute("weekEnd", task.getEndDate());
        return "taskInstances/details";
    }
}

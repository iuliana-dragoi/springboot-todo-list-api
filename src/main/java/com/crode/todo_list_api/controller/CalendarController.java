package com.crode.todo_list_api.controller;

import com.crode.todo_list_api.dto.TaskInstanceDto;
import com.crode.todo_list_api.model.TaskInstance;
import com.crode.todo_list_api.service.TaskInstanceService;
import com.crode.todo_list_api.service.TaskService;
import com.crode.todo_list_api.utils.TaskUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.*;

@Controller
@RequestMapping("/dashboard/calendar")
public class CalendarController {

    private final TaskService taskService;

    private final TaskInstanceService taskInstanceService;

    public CalendarController(TaskService taskService, TaskInstanceService taskInstanceService) {
        this.taskService = taskService;
        this.taskInstanceService = taskInstanceService;
    }

    @GetMapping()
    public String getCalendar(Model model) {
//        List<Task> tasks = taskService.getAllTasks();
//        List<Map<String, Object>> events = new ArrayList<>();
//        for (Task task : tasks) {
//            Map<String, Object> event = new HashMap<>();
//            event.put("title", task.getTitle());
//            event.put("start", task.getStartDate().toString());
//            event.put("end", task.getEndDate() != null ? task.getEndDate().toString() : null);
//            events.add(event);
//        }

        List<TaskInstance> tasks = taskInstanceService.getAll();
        List<TaskInstanceDto> taskInstanceDtos = getTaskInstancesDtos(tasks);

        List<Map<String, Object>> events = new ArrayList<>();
        for (TaskInstanceDto taskInstance : taskInstanceDtos) {
            Map<String, Object> event = new HashMap<>();
            event.put("title", taskInstance.getTitle());
            event.put("start", taskInstance.getDate());
            event.put("end", taskInstance.getDate());
            event.put("completed", taskInstance.getCompleted());
            event.put("type", taskInstance.getType().name());

            // Set different color based on completion status
            if (taskInstance.getCompleted()) {
                event.put("backgroundColor", "green");
                event.put("borderColor", "darkgreen");
            }
            else {
                event.put("backgroundColor", "red");
                event.put("borderColor", "darkred");
            }

            events.add(event);
        }

        model.addAttribute("tasks", events);
        return "calendar/calendar";
    }

    private List<TaskInstanceDto> getTaskInstancesDtos(List<TaskInstance> tasks) {
        return tasks.stream()
            .map(TaskUtil::taskInstanceToDto)
            .toList();
    }
}


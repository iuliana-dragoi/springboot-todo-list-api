package com.crode.todo_list_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import com.crode.todo_list_api.repository.TaskRepository;
import org.springframework.stereotype.Controller;
import com.crode.todo_list_api.model.Task;
import org.springframework.ui.Model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dashboard/calendar")
public class CalendarController {

    private final TaskRepository taskRepository;

    public CalendarController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping()
    public String getCalendar(Model model) {
        List<Task> tasks = taskRepository.findAll();

        List<Map<String, Object>> events = new ArrayList<>();
        for (Task task : tasks) {
            Map<String, Object> event = new HashMap<>();
            event.put("title", task.getTitle());
            event.put("start", task.getStartDate().toString());
            event.put("end", task.getEndDate() != null ? task.getEndDate().toString() : null);
            events.add(event);
        }

        model.addAttribute("tasks", events);
        return "calendar/calendar";
    }
}


package com.crode.todo_list_api.controller;

import com.crode.todo_list_api.model.TaskInstance;
import com.crode.todo_list_api.service.TaskInstanceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

}

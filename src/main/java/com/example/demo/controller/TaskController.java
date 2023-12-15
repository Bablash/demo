package com.example.demo.controller;

import com.example.demo.constant.Status;
import com.example.demo.dto.TaskDto;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping()
    public List<TaskDto> getAll() {
        return taskService.getAll();
    }

    @GetMapping("/{id}")
    public TaskDto get(@PathVariable Long id) {
        return taskService.get(id);
    }

    @GetMapping("/author/{authorId}")
    public List<TaskDto> getTasksByAuthor(@PathVariable Long authorId,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "5") int limit) {
        return taskService.getTasksByAuthor(authorId, PageRequest.of(page, limit));
    }

    @GetMapping("/executor/{executorId}")
    public List<TaskDto> getTasksByExecutor(@PathVariable Long executorId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "5") int limit) {
        return taskService.getTasksByExecutor(executorId, PageRequest.of(page, limit));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto create(@RequestBody TaskDto task) {
         return taskService.save(task);
    }

    @PutMapping
    public TaskDto update(@RequestBody TaskDto task) {
        return taskService.update(task);
    }

    @PatchMapping("/{id}/status")
    public TaskDto changeStatus(@PathVariable Long id, @RequestParam Status status) {
        return taskService.updateStatusTask(id, status);
    }

    @PatchMapping("/{id}/executor")
    public TaskDto changeExecutor(@PathVariable Long id, @RequestParam Long executorId) {
        return taskService.updateExecutorTask(id, executorId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        taskService.delete(id);
    }

}
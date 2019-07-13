package br.com.markstudy.testAPI.controller;

import br.com.markstudy.testAPI.domain.dto.TaskDTO;
import br.com.markstudy.testAPI.domain.entity.Task;
import br.com.markstudy.testAPI.service.interfaces.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController

public class TaskController {

    @Autowired
    TaskService service;

    @RequestMapping(value="/tasks/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public TaskDTO getTask(@PathVariable Long id){
        return service.findById(id);
    };

    @RequestMapping(value="/tasks/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public void deleteTask(@PathVariable Long id){
        service.deleteById(id);
    };

    @RequestMapping(value="/tasks/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public void updateTask(@PathVariable Long id, @RequestBody Task task){
        service.update(task, id);
    };

    @RequestMapping(value="/tasks", method = RequestMethod.POST, headers = "Accept=application/json")
    public void saveTask(@RequestBody Task entity){
        service.save(entity);
    }

    @RequestMapping(value="/tasks", method = RequestMethod.GET, headers = "Accept=application/json")
    public List<TaskDTO> getTaskList(@RequestParam("createdAt")  @DateTimeFormat(pattern="dd-mm-yyyy") Date createdAt){
        return service.findAllSortByCreatedAt(createdAt);
    }

}

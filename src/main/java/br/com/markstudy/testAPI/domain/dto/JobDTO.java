package br.com.markstudy.testAPI.domain.dto;

import java.util.List;

public class JobDTO {

    private Long id;
    private String name;
    private boolean active;
    private List<TaskDTO> tasks;
    private JobDTO parentJob;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
    }

    public JobDTO getParentJob() {
        return parentJob;
    }

    public void setParentJob(JobDTO parentJob) {
        this.parentJob = parentJob;
    }
}

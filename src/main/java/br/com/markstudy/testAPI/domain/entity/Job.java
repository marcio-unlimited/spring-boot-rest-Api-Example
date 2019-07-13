package br.com.markstudy.testAPI.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Job implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long cod;

    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private boolean active;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="ID_PARENT_JOB")
    private Job parentJob;

    @OneToMany(orphanRemoval = true, cascade=CascadeType.ALL)
    private List<Task> tasks;

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

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Job getParentJob() {
        return parentJob;
    }

    public void setParentJob(Job parentJob) {
        this.parentJob = parentJob;
    }

    public Long getCod() {
        return cod;
    }

    public void setCod(Long cod) {
        this.cod = cod;
    }

    public Job(Long id, String name, boolean active, Job parent, List<Task> tasks){
        this.id = id;
        this.name = name;
        this.active = active;
        this.parentJob = parent;
        this.tasks = tasks;
    }

    public Job(){}
}

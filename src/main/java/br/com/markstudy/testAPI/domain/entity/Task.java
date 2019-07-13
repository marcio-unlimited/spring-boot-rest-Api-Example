package br.com.markstudy.testAPI.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long cod;

    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private Integer weight;

    private Boolean completed;

    @Column(name = "CREATED_AT")
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_JOB")
    private Job job;

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

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCod() {
        return cod;
    }

    public void setCod(Long cod) {
        this.cod = cod;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Task(Long id, String name, Integer weight, Boolean completed, Date createdAt) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.completed = completed;
        this.createdAt = createdAt;
    }

    public Task(Long id, String name, Integer weight, Boolean completed, Date createdAt, Job parent) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.completed = completed;
        this.createdAt = createdAt;
        this.job = parent;
    }

    public Task(){}
}

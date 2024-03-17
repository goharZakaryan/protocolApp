package com.example.protocolapp.model;

import java.util.List;

public class Protocol {
    private Long id;
    private String name, taskList, taskListAuthor;
    private User user;
    private List<Step> steps;
    private Score score;


    public Protocol(Long id, String name, String taskList, String taskListAuthor, List<Step> steps, User user,Score score) {
        this.id = id;
        this.name = name;

        this.user = user;
        this.taskList = taskList;
        this.taskListAuthor = taskListAuthor;
        this.steps = steps;
        this.score = score;
    }
    public Protocol(Long id, String name, String taskList, String taskListAuthor, List<Step> steps, User user) {
        this.id = id;
        this.name = name;

        this.user = user;
        this.taskList = taskList;
        this.taskListAuthor = taskListAuthor;
        this.steps = steps;
    }

    public Protocol(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskList() {
        return taskList;
    }

    public void setTaskList(String taskList) {
        this.taskList = taskList;
    }

    public String getTaskListAuthor() {
        return taskListAuthor;
    }

    public void setTaskListAuthor(String taskListAuthor) {
        this.taskListAuthor = taskListAuthor;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }
}

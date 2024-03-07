package com.example.protocolapp.model;

import java.util.List;

public class Protocol {
    private String id, name, taskList,taskListAuthor;
    private List<Step> steps;

    public Protocol(String id, String name, String taskList, String taskListAuthor, List<Step> steps) {
        this.id = id;
        this.name = name;
        this.taskList = taskList;
        this.taskListAuthor = taskListAuthor;
        this.steps = steps;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}

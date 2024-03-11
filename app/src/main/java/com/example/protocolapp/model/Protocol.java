package com.example.protocolapp.model;

import java.util.List;

public class Protocol {
    private String  name, taskList,taskListAuthor, userEmail;
    private  User user;
    private List<Step> steps;

    public Protocol(String name, String taskList, String taskListAuthor, List<Step> steps, User user) {
        this.name = name;
        this.user=user;
        this.taskList = taskList;
        this.taskListAuthor = taskListAuthor;
        this.steps = steps;
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

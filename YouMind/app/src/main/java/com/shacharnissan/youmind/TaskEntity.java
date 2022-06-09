package com.shacharnissan.youmind;

import org.json.JSONObject;

import java.util.Date;

public class TaskEntity {
    private String id;
    private String name;
    private TaskLevelsEnum level;
    private Date createDate;
    private Date todoDate;
    private boolean isAcitive;

    public TaskEntity(String name, TaskLevelsEnum level){
        this(name,level,new Date());
    }

    public TaskEntity(String name){
        this(name,TaskLevelsEnum.GET_MOST_CASUAL_LEVEL(), new Date());
    }

    public TaskEntity(String name, TaskLevelsEnum level, Date todoDate){
        setName(name);
        setLevel(level);
        setTodoDate(todoDate);
        setAcitive(true);
        createDate = new Date();
    }

    public TaskEntity(String name, TaskLevelsEnum level, Date createDate, Date todoDate, boolean isActive) {
        this(null, name, level, createDate, todoDate, isActive);
    }

    public TaskEntity(String id, String name, TaskLevelsEnum level, Date createDate, Date todoDate, boolean isActive){
        setId(id);
        setName(name);
        setLevel(level);
        setCreateDate(createDate);
        setTodoDate(todoDate);
        setAcitive(isActive);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAcitive() {
        return isAcitive;
    }

    public void setAcitive(boolean acitive) {
        isAcitive = acitive;
    }

    public String getID() { return this.id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskLevelsEnum getLevel() {
        return level;
    }

    public void setLevel(TaskLevelsEnum level) {
        this.level = level;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getTodoDate() {
        return todoDate;
    }

    public void setTodoDate(Date todoDate) {
        this.todoDate = todoDate;
    }

}

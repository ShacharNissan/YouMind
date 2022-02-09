package com.shacharnissan.youmind;

import java.util.Date;

public class TaskEntity {
    private String name;
    private TaskLevelsEnum level;
    private Date createDate;
    private Date todoDate;

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
        createDate = new Date();
    }

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

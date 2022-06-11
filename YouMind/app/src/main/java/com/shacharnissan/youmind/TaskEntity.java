package com.shacharnissan.youmind;

import java.util.Date;

public class TaskEntity {
    private String id;
    private String name;
    private TaskSeverityEnum severity;
    private Date createDate;
    private Date todoDate;
    private boolean isActive;

    public TaskEntity(String name) {
        this(name, TaskSeverityEnum.GET_MOST_CASUAL_SEVERITY(), new Date(), true);
    }

    public TaskEntity(String name, TaskSeverityEnum severity) {
        this(name, severity,new Date(), true);
    }

    public TaskEntity(String name, TaskSeverityEnum severity, Date todoDate, boolean isActive) {
        this(null, name, severity, new Date(), todoDate, isActive);
    }

    public TaskEntity(String name, TaskSeverityEnum severity, Date createDate, Date todoDate, boolean isActive) {
        this(null, name, severity, createDate, todoDate, isActive);
    }

    public TaskEntity(String id, String name, TaskSeverityEnum severity, Date createDate, Date todoDate, boolean isActive){
        setId(id);
        setName(name);
        setSeverity(severity);
        setCreateDate(createDate);
        setTodoDate(todoDate);
        setActive(isActive);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getID() { return this.id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.isEmpty())
            throw new RuntimeException("Name can not be empty!");

        this.name = name;
    }

    public TaskSeverityEnum getSeverity() {
        return severity;
    }

    public void setSeverity(TaskSeverityEnum severity) {
        if (severity == null)
            throw new RuntimeException("Severity can not be empty!");

        this.severity = severity;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        if (createDate == null)
            throw new RuntimeException("createDate can not be empty!");

        this.createDate = createDate;
    }

    public Date getTodoDate() {
        return todoDate;
    }

    public void setTodoDate(Date todoDate) {
        if (todoDate == null)
            throw new RuntimeException("todoDate can not be empty!");

        this.todoDate = todoDate;
    }

}

package com.shacharnissan.youmind.data;

import java.util.Date;

public class TaskEntity extends YouMindEntity {
    private TaskSeverityEnum severity;
    private Date todoDate;
    private boolean isActive;

    public TaskEntity(String name, TaskSeverityEnum severity, Date todoDate, boolean isActive) {
        this(name, new Date(), severity, todoDate, isActive);
    }

    public TaskEntity(String name, Date createDate, TaskSeverityEnum severity, Date todoDate, boolean isActive) {
        this(null, name, createDate, severity, todoDate, isActive);
    }

    public TaskEntity(String id, String name, Date createDate, TaskSeverityEnum severity, Date todoDate, boolean isActive){
        super(id, name, createDate);
        setSeverity(severity);
        setTodoDate(todoDate);
        setActive(isActive);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public TaskSeverityEnum getSeverity() {
        return severity;
    }

    public void setSeverity(TaskSeverityEnum severity) {
        if (severity == null)
            throw new RuntimeException("Severity can not be empty!");

        this.severity = severity;
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

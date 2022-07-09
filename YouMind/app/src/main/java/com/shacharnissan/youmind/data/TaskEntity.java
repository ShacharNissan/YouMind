package com.shacharnissan.youmind.data;

import java.util.ArrayList;
import java.util.Date;

public class TaskEntity extends YouMindEntity {
    private TaskSeverityEnum severity;
    private Date todoDate;
    private boolean isActive;

    public TaskEntity(String name, ArrayList<String> labels,TaskSeverityEnum severity, Date todoDate, boolean isActive) {
        this(null, name, new Date(), labels, severity, todoDate, isActive);
    }

    public TaskEntity(String id, String name, Date createDate, ArrayList<String> labels, TaskSeverityEnum severity, Date todoDate, boolean isActive){
        super(id, name, createDate, labels);
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
            throw new RuntimeException("setSeverity: Severity can not be empty!");

        this.severity = severity;
    }

    public Date getTodoDate() {
        return todoDate;
    }

    public void setTodoDate(Date todoDate) {
        if (todoDate == null)
            throw new RuntimeException("setTodoDate: todoDate can not be empty!");

        this.todoDate = todoDate;
    }
}

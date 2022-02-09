package com.shacharnissan.youmind;

public class TaskEntity {
    private String name;
    private TaskLevelsEnum level;

    public TaskEntity(String name){
        this(name,TaskLevelsEnum.GET_MOST_CASUAL_LEVEL());
    }

    public TaskEntity(String Name, TaskLevelsEnum level){
        setName(Name);
        setLevel(level);
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
}

package com.shacharnissan.youmind.data;

public enum TaskSeverityEnum {
    EASY(3),
    MEDIUM(2),
    HARD(1);

    private final int value; // point per question

    TaskSeverityEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TaskSeverityEnum GET_MOST_CASUAL_SEVERITY(){
        int highestLevel = 0;
        TaskSeverityEnum highestEnum = null;

        for (TaskSeverityEnum taskSeverityEnum: TaskSeverityEnum.values()) {
            if (taskSeverityEnum.value > highestLevel) {
                highestLevel = taskSeverityEnum.value;
                highestEnum = taskSeverityEnum;
            }
        }

        return highestEnum;
    }
}

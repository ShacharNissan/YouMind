package com.shacharnissan.youmind;

public enum TaskLevelsEnum {
    EASY(3),
    MEDIUM(2),
    HARD(1);

    private final int value; // point per question

    TaskLevelsEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TaskLevelsEnum GET_MOST_CASUAL_LEVEL(){
        int highestLevel = 0;
        TaskLevelsEnum highestEnum = null;

        for (TaskLevelsEnum taskLevelEnum: TaskLevelsEnum.values()) {
            if (taskLevelEnum.value > highestLevel) {
                highestLevel = taskLevelEnum.value;
                highestEnum = taskLevelEnum;
            }
        }

        return highestEnum;
    }
}

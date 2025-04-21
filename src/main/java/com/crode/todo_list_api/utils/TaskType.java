package com.crode.todo_list_api.utils;

public enum TaskType {
    ONE_TIME,
    HABIT,
    RECURRING,
    GOAL,
    REMINDER,
    DEADLINE,
    CHALLENGE;

    public static TaskType fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return ONE_TIME;
        }
        try {
            return TaskType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ONE_TIME;
        }
    }
}


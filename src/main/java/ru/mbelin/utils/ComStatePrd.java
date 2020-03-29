package ru.mbelin.utils;

public enum ComStatePrd {
    READY("Ожидает"),
    PROCESSED("Работает"),
    STOPED("Остановлен");

    private String description;

    ComStatePrd(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "ComStatePrd{" +
                "description='" + description + '\'' +
                '}';
    }
}

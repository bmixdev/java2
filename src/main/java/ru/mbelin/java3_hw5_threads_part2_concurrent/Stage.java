package ru.mbelin.java3_hw5_threads_part2_concurrent;

public abstract class Stage {
    protected int length;
    protected String description;
    public String getDescription() {
        return description;
    }
    public abstract void go(Car c);
}
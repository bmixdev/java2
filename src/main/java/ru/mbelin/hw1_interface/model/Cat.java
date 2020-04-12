package ru.mbelin.hw1_interface.model;

import ru.mbelin.hw1_interface.impl.ImplRunAndJump;

import java.util.Objects;

public class Cat implements ImplRunAndJump {
    private String name;
    private int maxJump;
    private int maxRun;

    public Cat(String name, int maxJump, int maxRun) {
        this.name = name;
        this.maxJump = maxJump;
        this.maxRun = maxRun;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void info() {
        System.out.println("Кот " + this.name + " (макс. бег: " + this.maxRun + ", макс. прыжок: " + this.maxJump + ")");
    }

    @Override
    public String toString() {
        return "Кот " + this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cat cat = (Cat) o;
        return Objects.equals(name, cat.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public int run() {
        System.out.println(this + " бежит");
        return this.maxRun;
    }

    public int jump() {
        System.out.println(this + " прыгает");
        return this.maxJump;
    }
}

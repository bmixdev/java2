package ru.mbelin.hw1.model;

import ru.mbelin.hw1.impl.ImplRunAndJump;
import ru.mbelin.utils.Utils;

import java.util.Objects;

public class Human implements ImplRunAndJump {
    private String fio;
    private int maxJump;
    private int maxRun;

    public Human(String fio) {
        this.fio = fio;
        this.maxJump = Utils.getRandomInt(1, 5);
        this.maxRun = Utils.getRandomInt(100, 1000);
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Human human = (Human) o;
        return Objects.equals(fio, human.fio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fio);
    }

    public void info() {
        System.out.println(this.fio + " (макс. бег: " + this.maxRun + ", макс. прыжок: " + this.maxJump + ")");
    }

    @Override
    public String toString() {
        return this.fio;
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

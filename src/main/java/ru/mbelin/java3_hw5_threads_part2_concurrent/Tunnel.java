package ru.mbelin.java3_hw5_threads_part2_concurrent;

import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {
    static Semaphore semaphore;

    public Tunnel() {
        this.length = 80;
        this.description = "Тоннель " + length + " метров";
    }

    static {
        semaphore = new Semaphore(MainClass.CARS_COUNT / 2);
    }

    @Override
    public void go(Car c) {
        try {
            try {
                System.out.println("\t"+c.getName() + " готовится к этапу(ждет): " + description);
                semaphore.acquire();
                System.out.println("\t"+c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
                System.out.println("\t"+c.getName() + " закончил этап: " + description);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
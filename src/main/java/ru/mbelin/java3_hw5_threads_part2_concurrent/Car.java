package ru.mbelin.java3_hw5_threads_part2_concurrent;

import java.util.concurrent.atomic.AtomicInteger;

public class Car implements Runnable {
    private static int CARS_COUNT;
    static {
        CARS_COUNT = 0;
    }

    static AtomicInteger cntReady = new AtomicInteger(0);

    private Race race;
    private int speed;
    private String name;
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }
    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }
    @Override
    public void run() {
        try {
            int sleepT = 500 + (int)(Math.random() * 800);
            System.out.println("\t"+this.name + " готовится: "+sleepT+" мсек");
            Thread.sleep(sleepT);
            System.out.println("\t"+this.name + " готов");
            MainClass.countDownLatchReady.countDown();
            MainClass.cyclicBarrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        System.out.println(this.name + " завершил гонку");
        MainClass.setWinner(this);
        MainClass.countDownLatchFinish.countDown();
    }
}
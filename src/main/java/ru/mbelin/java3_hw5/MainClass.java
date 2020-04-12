package ru.mbelin.java3_hw5;

import sun.awt.windows.ThemeReader;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MainClass {
    public static final int CARS_COUNT = 4;

    public static ReentrantLock locker = new ReentrantLock();

    public static CountDownLatch countDownLatchReady = new CountDownLatch(CARS_COUNT); // количесвто защелок которые должны выполнится
    public static CyclicBarrier cyclicBarrier = new CyclicBarrier(CARS_COUNT); // для ожидания всех потоков в определенной точке
    public static CountDownLatch countDownLatchFinish = new CountDownLatch(CARS_COUNT); // количесвто защелок которые должны выполнится

    public static Car winner;

    public static void setWinner(Car car) {
        locker.lock();
        try {
            if (winner == null)
                    winner = car;
        }
        finally {
            locker.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {

            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");

            Race race = new Race(new Road(60), new Tunnel(), new Road(40));
            Car[] cars = new Car[CARS_COUNT];
            for (int i = 0; i < cars.length; i++) {
                cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
            }
            for (int i = 0; i < cars.length; i++) {
                Thread thread = new Thread(cars[i]);
                thread.start();
            }

        countDownLatchReady.await();
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
        countDownLatchFinish.await();
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
        System.out.println("Победитель: " + winner.getName());
    }
}




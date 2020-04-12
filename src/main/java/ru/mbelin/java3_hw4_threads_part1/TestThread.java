package ru.mbelin.java3_hw4_threads_part1;

public class TestThread {
    volatile int cnt = 0;
    static final int cntStream = 3;
    public static void main(String[] args) {
        TestThread lock = new TestThread();
        Thread t1 = new Thread(new Task("A", lock,1));
        Thread t2 = new Thread(new Task("B", lock,2));
        Thread t3 = new Thread(new Task("C", lock,3));
        t1.start();
        t2.start();
        t3.start();
    }
}

class Task implements Runnable {

    private final TestThread lock;
    private String msg;
    private int numStream;
    private int cntPrint = 0;

    Task(String msg, TestThread obj, int p) {
        this.msg = msg;
        this.lock = obj;
        this.numStream = p;
    }

    @Override
    public void run() {
        while(cntPrint < 5) {
            synchronized (lock) {
                while (!((lock.cnt % TestThread.cntStream) == (numStream - 1))) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

               System.out.print(msg);
               lock.cnt++;
               if ((lock.cnt % TestThread.cntStream) == 0) System.out.print(" ");
               cntPrint++;
               lock.notifyAll();
            }
        }
    }
}
package ru.mbelin.hw5;

import ru.mbelin.utils.Timer;
import ru.mbelin.utils.Utils;
import sun.awt.windows.ThemeReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayCalc {
    private int size;
    private int cntThread;
    private int h;
    private float[] arr;

    public ArrayCalc(int size, int cntThread) {
        this.size = size;
        this.cntThread = cntThread;
        this.h = size / cntThread;
        this.arr = new float[size];
    }

    private void initializeArray() {
        Timer.getInstance().set("initializeArray");
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 1;
        }
        Timer.getInstance().get("initializeArray");
    }

    public int getCntThread() {
        return this.cntThread;
    }

    public void print() {
        Utils.printArray(arr);
    }

    public void recalc(float[] arr, int threadPos) {
        Timer.getInstance().set(Thread.currentThread().getName() + ": recalc");
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + (i + threadPos) / 5) * Math.cos(0.2f + (i + threadPos) / 5) * Math.cos(0.4f + (i + threadPos) / 2));
        }
        Timer.getInstance().get(Thread.currentThread().getName() + ": recalc");
    }

    public void recalcInMain() {
        Timer.getInstance().set("recalcInMain");
        initializeArray();
        recalc(this.arr,0);
        Timer.getInstance().get("recalcInMain");
    }

    // как написано сделать в практическом задании
    public void recalcIn2Threads() {
        Timer.getInstance().set("recalcIn2Threads");
        initializeArray();
        int hh = size / 2;
        float[] arr1 = new float[hh];
        float[] arr2 = new float[hh];
        System.arraycopy(arr, 0, arr1, 0, hh);
        System.arraycopy(arr, hh, arr2, 0, hh);
        Thread thread1 = new Thread(()->{recalc(arr1, 0);}, "TR1");
        Thread thread2 = new Thread(()->{recalc(arr2, hh);}, "TR2");
        try {
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.arraycopy(arr1, 0, arr, 0, hh);
        System.arraycopy(arr2, 0, arr, hh, hh);
        Timer.getInstance().get("recalcIn2Threads");
    }

    // моя реализация
    public void recalcInThreadsOthers() {
        Timer.getInstance().set("recalcInThreads");
        initializeArray();
        List<float[]> listArrays = new ArrayList<>();
        List<Thread> listThreads = new ArrayList<>();

        int pos = 0;
        int idx = 0;
        int start = 0;
        do {
            idx++;
            start = pos;
            pos += Math.round(h);
            if (idx == cntThread && (pos < arr.length)) {
                pos += arr.length - pos;
            }
            listArrays.add(Arrays.copyOfRange(arr, start, pos));
            int finalStart = start;
            Thread thread = new Thread(() -> recalc(listArrays.get(listArrays.size() - 1), finalStart), "MyThread#"+idx);
            listThreads.add(thread);
            thread.start();
        } while (pos < arr.length);
        // ожидание потоков
        for (Thread t: listThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        pos = 0;
        for (float[] f: listArrays) {
            System.arraycopy(f, 0, arr, pos, f.length);
            pos += Math.round(h);
        }

        Timer.getInstance().get("recalcInThreads");
    }


}
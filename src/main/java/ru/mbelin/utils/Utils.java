package ru.mbelin.utils;

import java.util.Random;
import java.util.Scanner;

public class Utils {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static int getRandomInt(int min, int max) {
        return (int)(Math.random()*((max-min)+1))+min;
    }

    public static int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }


    public static double getRandom(int min, int max) {
        return (Math.random()*((max-min)+1))+min;
    }

    public static int[] generateArray(int size) {
        int[] ints = new int[size];
        for (int i = 0; i < ints.length; i++)
            ints[i] = getRandomInt(-100, 100);
        return ints;
    }
    public static String getUserConsoleInput(String msg) {
        if (msg != null || !msg.isEmpty())
            System.out.print(msg);
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        return input;
    }

    public static void printArray(int[] ints) {
        System.out.print("\t[");
        for (int i =0; i <= ints.length-1; i++) System.out.print(ints[i] + (i < ints.length - 1 ? "," : "") );
        System.out.print("]\n");
    }

    public static void printArray(float[] ints) {
        System.out.print("\t[");
        for (int i =0; i <= ints.length-1; i++) System.out.print(ints[i] + (i < ints.length - 1 ? "," : "") );
        System.out.print("]\n");
    }


    public static void printArray(int[] ints,  int markIndex) {
        System.out.print("\t[");
        for (int i =0; i <= ints.length-1; i++) System.out.print((i == markIndex ? "||" : "") + ints[i] + (i < ints.length - 1 ? "," : "") );
        System.out.print("]\n");
    }

    public static void printArray(int[][] ints) {

        for (int i = 0; i < ints.length; i++) {
            for (int j = 0; j < ints[i].length; j++) {
                if (j == 0) System.out.print("\t[");
                System.out.print(ints[i][j] + (j < ints[i].length - 1 ? ", " : ""));
            }
            System.out.print("]\n");
        }
    }

    public static void printArray(char[][] ints) {

        for (int i = 0; i < ints.length; i++) {
            for (int j = 0; j < ints[i].length; j++) {
                if (j == 0) System.out.print("\t[");
                System.out.print(ints[i][j] + (j < ints[i].length - 1 ? ", " : ""));
            }
            System.out.print("]\n");
        }
    }

    public static void printArray(String[][] ints) {

        for (int i = 0; i < ints.length; i++) {
            for (int j = 0; j < ints[i].length; j++) {
                if (j == 0) System.out.print("\t[");
                System.out.print(ints[i][j] + (j < ints[i].length - 1 ? ", " : ""));
            }
            System.out.print("]\n");
        }
    }

    public static int[] inputArray() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите размерность массива: ");
        int n = scanner.nextInt();
        int[] resArray = new int[n];
        for (int i = 0; i < resArray.length; i++) {
            System.out.println("Введите число:");
            resArray[i] = scanner.nextInt();
        }
        return resArray;
    }

    // Сдвинуть массив, новые позиции добиваются нулями
    public static int[] shiftArray(int[] srcArray, int shift) {
        int absShift = Math.abs(shift);
        int[] newInts = new int[srcArray.length + absShift];
        int cnt = newInts.length;
        while (--cnt >= 0) {
            if (shift >= 0)
                if (cnt >= absShift) newInts[cnt] = srcArray[cnt - absShift];
                else newInts[cnt] = 0;
            else
            if (cnt >= srcArray.length) newInts[cnt] = 0;
            else newInts[cnt] = srcArray[cnt];
        }
        return newInts;
    }

    public final static void clearConsole()
    {
        try
        {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
            {
                Runtime.getRuntime().exec("cls");
            }
            else
            {
                Runtime.getRuntime().exec("clear");
            }
        }
        catch (final Exception e)
        {
            //  Handle any exceptions.
        }
    }

}
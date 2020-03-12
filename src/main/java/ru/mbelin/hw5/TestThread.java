package ru.mbelin.hw5;

import ru.mbelin.utils.Color;
import ru.mbelin.utils.ConsoleColors;

public class TestThread {

    public static void main(String[] args) {
            ArrayCalc array= new ArrayCalc(100_000_000, 12);
            ConsoleColors.print("Расчет массива в основном потоке", Color.GREEN);
            array.recalcInMain();
            //array.print();

            ConsoleColors.print("Расчет массива в 2х отдельных потоках", Color.GREEN);
            array.recalcIn2Threads();

            ConsoleColors.print("Расчет массива в " + array.getCntThread() + "х отдельных потоках", Color.GREEN);
            array.recalcInThreadsOthers();
            //array.print();
    }

}

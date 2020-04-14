package ru.mbelin.java3_hw6_logger_test;

import ru.mbelin.utils.Utils;

import java.util.Arrays;
import java.util.logging.Logger;
/*
Написать метод, которому в качестве аргумента передается не пустой одномерный целочисленный массив.
Метод должен вернуть новый массив, который получен путем вытаскивания из исходного массива элементов, идущих после последней четверки.
Входной массив должен содержать хотя бы одну четверку, иначе в методе необходимо выбросить RuntimeException.
Написать набор тестов для этого метода (по 3-4 варианта входных данных). Вх: [ 1 2 4 4 2 3 4 1 7 ] -> вых: [ 1 7 ].
 */
public class Task2 {
    private static final Logger log = Logger.getLogger(Task2.class.getName());

    public static void main(String[] args) {
        final int[] res2 = process(new int[]{1, 2, 4, 3, 5, 4, 6, 7, 8});
        Utils.printArray(res2);
    }

    public static int[] process(int[] arrIn) {
        int[] backArr = new int[2];
        if(arrIn.length == 0) {
            log.info("В массиве нет элементов");
            return arrIn;
        }
        for (int i = arrIn.length - 1; i >= 0 ; i--) {
            if(arrIn[i] == 4) {
                backArr = Arrays.copyOfRange(arrIn, i + 1, arrIn.length);
                return backArr;
            }
        }
        throw new RuntimeException("В массиве нет ни одной 4-ки после которой были бы числа");
    }
}
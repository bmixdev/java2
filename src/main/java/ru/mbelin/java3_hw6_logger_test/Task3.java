package ru.mbelin.java3_hw6_logger_test;

/*
3. Написать метод, который проверяет состав массива из чисел 1 и 4.
Если в нем нет хоть одной четверки или единицы, то метод вернет false;
Написать набор тестов для этого метода (по 3-4 варианта входных данных).
 */

public class Task3 {
    public static void main(String[] args) {
        final boolean result = process(new int[]{0, 3, 0, 0, 1});
        System.out.println(result);
    }

    public static boolean process(int[] arrIn) {
        for (int i: arrIn) {
            if (i == 1 || i == 4) return true;
        }
        return false;
    }
}
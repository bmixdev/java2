package ru.mbelin.hw2;

import ru.mbelin.utils.Color;
import ru.mbelin.utils.ConsoleColors;

public class  TestArray {
    public static void main(String[] args) {
        //###################################################################################################################
        ConsoleColors.print("#Тест_1# Заполнение массива и генерация исключений", Color.BLUE);
        Array4x4 array4x4 = null;
        try {
            array4x4 = new Array4x4(4);
            ConsoleColors.print("#Тест_1# Размерность 4x4 рандом", Color.GREEN);
            array4x4.print();
            ConsoleColors.print("#Тест_1# Получение суммы всех элементов массива", Color.GREEN);
            try {
                ConsoleColors.print("\tСумма всех элементов массива: " + array4x4.sumAllElements(), Color.YELLOW);
            } catch (MyArrayDataException e) {
                System.out.println(e);
              //  e.printStackTrace();
            }
        } catch (MyArraySizeException e) {
            System.out.println(e.getMessage());
          //  e.printStackTrace();
        }
        //###################################################################################################################
        ConsoleColors.print("#Тест_2# Заполнение массива и генерация исключений# Массив ручной", Color.BLUE);
        String[][] tmpArray = { {"1","1","1","1"},
                              {"2","2","2","2"},
                              {"3","3","3","3"},
                              {"4","4","4","4"}
                            };
        try {
            array4x4 = new Array4x4(tmpArray);
            array4x4.print();
            ConsoleColors.print("#Тест_2# Получение суммы всех элементов массива", Color.GREEN);
            ConsoleColors.print("\tСумма всех элементов массива: " + array4x4.sumAllElements(), Color.YELLOW);
        } catch (MyArraySizeException e) {
            e.printStackTrace();
        }
        //###################################################################################################################
        ConsoleColors.print("#Тест_3# Заполнение массива не правильной длины", Color.BLUE);
        try {
            array4x4 = new Array4x4(10);
            array4x4.print();
        } catch (MyArraySizeException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        ConsoleColors.print("Это сообщение не должно появиться!", Color.GREEN);
    }
}

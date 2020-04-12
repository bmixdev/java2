package ru.mbelin.hw2_throws;

import ru.mbelin.utils.Utils;

public class Array4x4  {
    private static final int DEFAULT_SIZE = 4;
    private static final String[] AVAILABLE_SYMBOL = {"A","B","C","D","E","F", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private String[][] arr;

    public Array4x4(int size) throws MyArraySizeException {
        if (size != DEFAULT_SIZE)
            throw new MyArraySizeException(DEFAULT_SIZE);
        this.arr = new String[size][size];
        fillRandomArray();
    }

    public Array4x4(String[][] arr) throws MyArraySizeException {
        if ((arr.length != DEFAULT_SIZE) && (arr[0].length != DEFAULT_SIZE))
                throw new MyArraySizeException(DEFAULT_SIZE);
        this.arr = arr;
    }

    public void fillRandomArray() {
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            for (int j = 0; j < DEFAULT_SIZE; j++) {
                arr[i][j] = AVAILABLE_SYMBOL[Utils.getRandomNumberUsingNextInt(0, AVAILABLE_SYMBOL.length - 1)];
            }
        }
    }

    public void print() {
        Utils.printArray(arr);
    }

    public int sumAllElements() {
        int sumTotal = 0;
        for (int i = 0; i < DEFAULT_SIZE ; i++) {
            for (int j = 0; j < DEFAULT_SIZE ; j++) {
              try {
                      sumTotal += Integer.valueOf(arr[i][j]);
              }
              catch (NumberFormatException e) {
                  throw new MyArrayDataException("Значение не является числом. Элемента ["+i+"]["+j+"]: " + arr[i][j]);
              }
            }

        }
        return sumTotal;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (j == 0) sb.append("\t[");
                sb.append(arr[i][j] + (j < arr[i].length - 1 ? ", " : ""));
            }
            sb.append("]\n");
        }
        return sb.toString();
    }
}

package ru.mbelin.hw2_throws;

public class MyArraySizeException extends Exception {
    private int availableSize;
    public MyArraySizeException(int availableSize) {
        this.availableSize = availableSize;
    }

    @Override
    public String toString() {
        return "MyArraySizeException[Указан массив не допустимой размерности. Допустимое значение равно "+availableSize+"]";
    }

    @Override
    public String getMessage() {
        return toString();
    }
}

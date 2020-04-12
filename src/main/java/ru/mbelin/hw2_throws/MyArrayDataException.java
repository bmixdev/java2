package ru.mbelin.hw2_throws;

public class MyArrayDataException extends RuntimeException {

    private String detail;


    public MyArrayDataException(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "MyArraySumException["+detail+"]";
    }
}

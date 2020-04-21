package ru.mbelin.java3_hw7_reflection_annotation;

public class MainTask {

    public static void main(String[] args) {
        ClassToTest classToTest = new ClassToTest();
        TestFactory.start(classToTest.getClass());
    }
}

package ru.mbelin.java3_hw7_reflection_annotation;

import java.lang.reflect.Method;

public class ClassToTest {
    @BeforeSuite
    public void initialize() {
        System.out.println("initialize: Метод выполнить в первую очередь");
    }

    @Test(priority = 1)
    public void test1() {
        Method method = null;
        try {
            method = this.getClass().getMethod("test1");
            Test test = method.getAnnotation(Test.class);
            System.out.println("Выполняю test1 приоритет " + test.priority());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 1)
    public void test2() {
        Method method = null;
        try {
            method = this.getClass().getMethod("test2");
            Test test = method.getAnnotation(Test.class);
            System.out.println("Выполняю test2 приоритет " + test.priority());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    @Test(priority = 5)
    public void test3() {
        Method method = null;
        try {
            method = this.getClass().getMethod("test3");
            Test test = method.getAnnotation(Test.class);
            System.out.println("Выполняю test3 приоритет " + test.priority());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    @Test(priority = 2)
    public void test4() {
        Method method = null;
        try {
            method = this.getClass().getMethod("test4");
            Test test = method.getAnnotation(Test.class);
            System.out.println("Выполняю test4 приоритет " + test.priority());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @AfterSuite
    public void finalize() {
        System.out.println("finalize: Метод выполнить в последнюю очередь");
    }


    /*
    @AfterSuite
    public void finalizeCopy() {
        System.out.println("finalizeCopy: Метод выполнить в последнюю очередь");
    }

     */

}

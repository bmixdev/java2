package ru.mbelin.java3_hw7_reflection_annotation;
/*

1. Создать класс, который может выполнять «тесты», в качестве тестов выступают классы с наборами методов с аннотациями @Test.
Для этого у него должен быть статический метод start(), которому в качестве параметра передается или объект типа Class, или имя класса.
Из «класса-теста» вначале должен быть запущен метод с аннотацией @BeforeSuite, если такой имеется, далее запущены методы с аннотациями @Test,
а по завершению всех тестов – метод с аннотацией @AfterSuite. К каждому тесту необходимо также добавить приоритеты (int числа от 1 до 10),
в соответствии с которыми будет выбираться порядок их выполнения, если приоритет одинаковый, то порядок не имеет значения.
Методы с аннотациями @BeforeSuite и @AfterSuite должны присутствовать в единственном экземпляре,
иначе необходимо бросить RuntimeException при запуске «тестирования».

*/

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestFactory {

    private static List<Method> listBeforeMethods;
    private static List<Method> listAfterMethods;
    private static List<Method> listTestMethods;

    private static Object obj;

    private static Class aClass;

    public static void start(Class clazz) {
       listBeforeMethods = new ArrayList<>();
       listAfterMethods = new ArrayList<>();
       listTestMethods = new ArrayList<>();
        try {
            obj = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        aClass = clazz;
       getMethods();
       if (listBeforeMethods.size() > 1)
           throw new RuntimeException("Метод с анатацией BeforeSuite должен быть в одном экземпляре!");
       if (listAfterMethods.size() > 1)
           throw new RuntimeException("Метод с анатацией AfterSuite должен быть в одном экземпляре!");

       listTestMethods.sort((o1, o2) -> o1.getAnnotation(Test.class).priority() - o2.getAnnotation(Test.class).priority());

        invokeMethods(listBeforeMethods);
        invokeMethods(listTestMethods);
        invokeMethods(listAfterMethods);

    }


    private static void getMethods() {
        for ( Method method : aClass.getDeclaredMethods())
        {
            if (method.getAnnotation(BeforeSuite.class) != null)
                listBeforeMethods.add(method);
            else if (method.getAnnotation(AfterSuite.class) != null)
                listAfterMethods.add(method);
            else if (method.getAnnotation(Test.class) != null)
                listTestMethods.add(method);
        }
    }

    private static void invokeMethods(List<Method> list) {
        for (Method m: list) {
            try {
                m.setAccessible(true);
                m.invoke(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

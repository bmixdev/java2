package ru.mbelin.java3_hw1;

import sun.security.util.ArrayUtil;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;

class ArrayChangeElement<T> {

    private static ArrayChangeElement instance;

    public static ArrayChangeElement getInstance() {
        if (instance == null)
            instance = new ArrayChangeElement();
        return instance;
    }

    public void changeElement(T[] array, int one, int two) {
        try {
            T tmpOne = array[one];
            array[one] = array[two];
            array[two] = tmpOne;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.printf("Элемент [%s] находится за пределами массива%n", e.getMessage());
        }
    }

    public void printElement(T... array) {
        System.out.print("[");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + (i == array.length - 1 ? "" : ","));
        }
        System.out.print("]\n");
    }

    public List<T> getArrayList(T... array) {
        return Arrays.asList(array);
    }

}

public class GenericsDemoApp {

    public static void main(String[] args) {
        testTask1();
        testTask2();
        testTask3();
    }

    private static void testTask1() {
        System.out.println("Задание#1");
        ArrayChangeElement arrayHepler =  ArrayChangeElement.getInstance();

        System.out.println("Целочисленный массив:");
        Integer[] integers = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        System.out.println("до:");
        arrayHepler.printElement(integers);
        arrayHepler.changeElement(integers, 0, 9);
        System.out.println("после:");
        arrayHepler.printElement(integers);

        System.out.println("Строковый массив:");
        String[] strings = new String[]{"a","b","c","d"};
        System.out.println("до:");
        arrayHepler.printElement(strings);
        arrayHepler.changeElement(strings, 2, 3);
        System.out.println("после:");
        arrayHepler.printElement(strings);
    }

    private static void testTask2() {
        System.out.println("Задание#2");
        ArrayChangeElement arrayHepler =  ArrayChangeElement.getInstance();
        System.out.println(arrayHepler.getArrayList(new Integer[]{1,2,3,4,5}));
        System.out.println(arrayHepler.getArrayList(new String[]{"A1", "B2", "C3", "D4", "E5"}));
        System.out.println(arrayHepler.getArrayList(new Boolean[]{true, false, true, true}));
    }

    private static void testTask3() {
        Box<Apple> appleBox = new Box<>();
        Box<Orange> orangeBox = new Box<>();
        for (int i=0; i < 10; i++) {
            appleBox.addFruit(new Apple());
        }
        appleBox.info();

        for (int i=0; i < 5; i++) {
            orangeBox.addFruit(new Orange());
        }
        orangeBox.info();
        System.out.printf("Ящики %s%n", appleBox.compare(orangeBox) ? "равны" : "не равны");

        Box<Apple> emptyAppleBox = new Box<>();
        appleBox.moveFruitToOtherBox(emptyAppleBox);
        System.out.println("Информация о старой коробке");
        appleBox.info();
        System.out.println("Информация о новой коробке");
        emptyAppleBox.info();
    }

}

class Fruit{
    float weigth = 0;
    public Fruit() {
        this.weigth = 0;
    }

    public float getWeigth() {
        return weigth;
    }
}
class Apple extends Fruit{
    float weigth;
    public Apple() {
        this.weigth = 1.0f;
    }

    @Override
    public float getWeigth() {
        return weigth;
    }
}
class Orange extends Fruit{
    float  weigth;
    public Orange() {
        this.weigth = 1.5f;
    }

    @Override
    public float getWeigth() {
        return weigth;
    }
}

class Box<T extends Fruit> {
    private ArrayList<T> fruits;

    public Box(){
        this.fruits = new ArrayList<>();
    }

    public Box<T> addFruit(T fruit) {
        this.fruits.add(fruit);
        return this;
    }

    public ArrayList<T> getFruits() {
        return this.fruits;
    }

    public float getWeight() {
        if (this.fruits.size() == 0)
            return 0;
        return this.fruits.size() * this.fruits.get(0).getWeigth();
    }

    public boolean compare(Box<?> otherBox) {
        return (this.getWeight() == otherBox.getWeight());
    }

    public void moveFruitToOtherBox(Box<T> otherBox) {
        otherBox.getFruits().addAll(0, this.fruits);
        fruits.clear();
    }

    public void info() {
        System.out.printf("Кол-во фруктов в ящике: %s%nОбщий вес ящика: %s%n", this.fruits.size(), this.getWeight());
    }

    @Override
    public String toString() {
        return "Box{" +
                "fruits=" + fruits +
                '}';
    }
}
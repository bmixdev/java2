package ru.mbelin.hw3;

import ru.mbelin.utils.Color;
import ru.mbelin.utils.ConsoleColors;

public class Test2 {

    public static void main(String[] args) {
        // заполнение справочника
        ContactsFactory phoneBook = ContactsFactory.getPhoneBook();
        phoneBook.add("Михаил", 89261111111L, 89262222222L);
        phoneBook.add("Иван", 89151111111L);
        phoneBook.add("Анна", 89031111111L, 89032222222L, 89033333333L);
        phoneBook.add("Иван", 89011111111L, 89012222222L);
        // печать полного списка
        System.out.println(phoneBook);
        // поиск контактов и вывод телефонов
        ConsoleColors.print("Поиск конкретного контакта:", Color.YELLOW);
        String contactName = "Михаил";
        System.out.println(contactName + ": " + phoneBook.get(contactName));
        contactName = "Иван";
        System.out.println(contactName + ": " + phoneBook.get(contactName));
        // удаление телефона у конкретного контакта
        Contact contact = phoneBook.getСontact("Анна");
        contact.deletePhone(89032222222L);
        System.out.println(contact.getName() + ": " + phoneBook.get(contact.getName()));
        // удаление контакта
        phoneBook.delete("Иван");
        // добавление нового телефона контакту
        phoneBook.getСontact("Михаил").addPhone(89990001122L);
        // еще раз печать телефонной книжки
        System.out.println(phoneBook);
    }

}

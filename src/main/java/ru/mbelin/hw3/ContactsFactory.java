package ru.mbelin.hw3;

import ru.mbelin.utils.ConsoleColors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ContactsFactory {
    private List<Contact> contactList;
    private static ContactsFactory instance;

    private ContactsFactory() {
        this.contactList = new ArrayList<Contact>();
    }

    public static ContactsFactory getPhoneBook() {
        if (instance == null) {
            instance = new ContactsFactory();
        }
        return instance;
    }

    public void add(String name, Long... phoneNum) {
        Contact contact = new Contact(name);
        contact.addPhone(phoneNum);
        this.contactList.add(contact);
    }

    public List<Long> get(String name) {
        List<Long> result = new ArrayList<Long>();

        Iterator<Contact> iter = this.contactList.iterator();
        while (iter.hasNext()) {
           Contact contact = iter.next();
           if (contact.getName().equals(name)) {
               result.addAll(contact.getPhones());
           }
        }
        return result;
    }

    public Contact getСontact(String name) {
        for (Contact c: this.contactList) {
            if (c.getName().equals(name)) return c;
        }
        return null;
    }

    public void delete(String name) {
        List<Contact> removeList = new ArrayList<>();
        for (Contact c: this.contactList) {
            if (c.getName().equals(name)) removeList.add(c);
        }
        this.contactList.removeAll(removeList);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ConsoleColors.PURPLE_BOLD).append("\n").append("Телефонный справочник:\n");
        this.contactList.forEach(contact -> sb.append("\t").append(contact).append("\n"));
        sb.append(ConsoleColors.RESET);
        return sb.toString();
    }
}

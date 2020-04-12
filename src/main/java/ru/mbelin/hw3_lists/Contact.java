package ru.mbelin.hw3_lists;

import java.util.*;

public class Contact {
    private String name;
    private List<Long> phones;

    public Contact(String name) {
        this.name = name;
        this.phones = new ArrayList<Long>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPhone(Long... phoneNums) {
        this.phones.addAll(Arrays.asList(phoneNums));
    }

    public void deletePhone(Long... phoneNums) {
        this.phones.removeAll(Arrays.asList(phoneNums));
    }

    public List<Long> getPhones() {
        return this.phones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(name, contact.name) &&
                Objects.equals(phones, contact.phones);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phones);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", phones=" + phones +
                '}';
    }
}

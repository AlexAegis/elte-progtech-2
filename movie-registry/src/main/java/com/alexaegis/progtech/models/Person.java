package com.alexaegis.progtech.models;

import com.alexaegis.progtech.database.PersonTypes;

import java.util.Date;

public class Person {

    private int id;
    private String name;
    private Date birth;
    private PersonTypes type;

    public Person(int id, String name, Date birth, PersonTypes type) {
        this.id = id;
        this.name = name;
        this.birth = birth;
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        return name != null ? name.equals(person.name) : person.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public PersonTypes getType() {
        return type;
    }

    public Date getBirth() {
        return birth;
    }

    public int getId() {
        return id;
    }
}
package com.alexaegis.progtech.models;

import java.util.Date;

public class Person {

    private int id;
    private String name;
    private Date birth;

    public Person(int id, String name, Date birth) {
        this.id = id;
        this.name = name;
        this.birth = birth;
    }

    @Override
    public String toString() {
        return name;
    }
}
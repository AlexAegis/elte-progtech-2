package com.alexaegis.progtech.models.people;

import java.util.Date;

public class Person {

    private long id;
    private Name name;
    private Date birth;
    private PersonTypes type;

    public Person(long id, Name name, Date birth, PersonTypes type) {
        this.id = id;
        this.name = name;
        this.birth = birth;
        this.type = type;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        //if (id != person.id) return false;
        if (name != null ? !name.equals(person.name) : person.name != null) return false;
        //if (birth != null ? !birth.equals(person.birth) : person.birth != null) return false;
        return type == person.type;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (birth != null ? birth.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name.toString() + ", " + type.toString();
    }

    public Name getName() {
        return name;
    }

    public PersonTypes getType() {
        return type;
    }

    public Date getBirth() {
        return birth;
    }

    public long getId() {
        return id;
    }
}
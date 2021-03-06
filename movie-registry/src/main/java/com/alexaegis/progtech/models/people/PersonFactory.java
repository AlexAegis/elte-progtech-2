package com.alexaegis.progtech.models.people;

import com.alexaegis.progtech.misc.IllegalPersonException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class PersonFactory {

    private static List<Long> ids = new ArrayList<>();

    private PersonFactory() {

    }

    public static Person createPerson(long id, String name, Date birth, PersonTypes personType) throws IllegalPersonException {
        Name nameObj = new NameBuilder(name).getName();
        return createPerson(id, nameObj, birth, personType);
    }

    public static Person createPerson(long id, Name name, Date birth, PersonTypes personType) throws IllegalPersonException {
        if(personType.equals(PersonTypes.ACTOR)) return createActor(id, name, birth);
        else if(personType.equals(PersonTypes.DIRECTOR)) return createDirector(id, name, birth);
        else return null;
    }

    public static Person createPerson(long id, String name, String personType) throws IllegalPersonException {
        if(personType.toLowerCase().equals(PersonTypes.ACTOR.toString().toLowerCase())) return createPerson(id, name, new Date(), PersonTypes.ACTOR);
        else return createPerson(id, name, new Date(), PersonTypes.DIRECTOR);
    }

    public static Person createActor(long id, Name name, Date birth) throws IllegalPersonException {
        //if(!birth.before(new Date())) throw new InvalidBirthDateExceptionIllegal("Cannot be born in the future.");
        Person actor = new Person(id, name, birth, PersonTypes.ACTOR);
        ids.add(id);
        return actor;
    }

    public static Person createActor(long id, String name, Date birth) throws IllegalPersonException {
        Name nameObj = new NameBuilder(name).getName();
        return createActor(id, nameObj, birth);
    }

    public static Person createDirector(long id, Name name, Date birth) throws IllegalPersonException {
        //if(!birth.before(new Date())) throw new InvalidBirthDateExceptionIllegal("Cannot be born in the future.");
        Person director = new Person(id, name, birth, PersonTypes.DIRECTOR);
        ids.add(id);
        return director;
    }

    public static Person createDirector(long id, String name) throws IllegalPersonException {
        return createDirector(id, name, new Date());
    }

    public static Person createActor(long id, String name) throws IllegalPersonException {
        return createActor(id, name, new Date());
    }

    public static Person createDirector(long id, String name, Date birth) throws IllegalPersonException {
        Name nameObj = new NameBuilder(name).getName();
        return createDirector(id, nameObj, birth);
    }

    public static List<Long> getIds() {
        return ids;
    }

    public int getTotal() {
        return ids.size();
    }

}
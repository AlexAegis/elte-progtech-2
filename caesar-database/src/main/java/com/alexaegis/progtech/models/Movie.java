package com.alexaegis.progtech.models;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Vector;

public class Movie {

    private int id;
    private String name;
    private Date release;
    private List<Person> directors = new ArrayList<>();
    private List<Person> actors = new ArrayList<>();

    private String owner;
    private boolean legal;

    public Movie(int id, String name, Date release) {
        this.id = id;
        this.name = name;
        this.release = release;
    }

    public void addDirector(Person director) {
        directors.add(director);
    }

    public void addActor(Person actor) {
        actors.add(actor);
    }

    public Vector<Object> getData() {
        Vector<Object> data = new Vector<>();
        data.add(id);
        data.add(name);
        data.add(release);
        data.add(directors);
        data.add(actors);
        return data;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", release=" + release +
                ", directors=" + directors +
                ", actors=" + actors +
                ", owner='" + owner + '\'' +
                ", legal=" + legal +
                '}';
    }
}
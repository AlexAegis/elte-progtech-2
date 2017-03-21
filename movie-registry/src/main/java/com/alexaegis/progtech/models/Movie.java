package com.alexaegis.progtech.models;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Vector;

public class Movie {

    private long id;
    private String title;
    private Date release;
    private List<Person> directors = new ArrayList<>();
    private List<Person> actors = new ArrayList<>();

    private String owner;
    private boolean legal;

    public Movie(long id, String title, Date release) {
        this.id = id;
        this.title = title;
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
        data.add(title);
        data.add(release);
        data.add(directors);
        data.add(actors);
        return data;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", release=" + release +
                ", directors=" + directors +
                ", actors=" + actors +
                ", owner='" + owner + '\'' +
                ", legal=" + legal +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    public Date getRelease() {
        return release;
    }

    public List<Person> getActors() {
        return actors;
    }

    public List<Person> getDirectors() {
        return directors;
    }
}
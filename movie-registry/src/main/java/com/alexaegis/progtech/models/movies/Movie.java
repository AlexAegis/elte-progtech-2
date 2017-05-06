package com.alexaegis.progtech.models.movies;

import com.alexaegis.progtech.models.people.Person;
import com.alexaegis.progtech.models.people.PersonTypes;

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

    public Movie(long id, String title, Date release, boolean legal) {
        this.id = id;
        this.title = title;
        this.release = release;
        this.legal = legal;
    }

    public void addPerson(Person person) {
        System.out.println(person);
        if(person.getType().equals(PersonTypes.DIRECTOR)) directors.add(person);
        else actors.add(person);
    }

    public Vector<Object> getData() {
        Vector<Object> data = new Vector<>();
        data.add(id);
        data.add(title);
        data.add(release);
        data.add(directors);
        data.add(actors);
        data.add(legal);
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (id != movie.id) return false;
        if (legal != movie.legal) return false;
        if (title != null ? !title.equals(movie.title) : movie.title != null) return false;
        if (release != null ? !release.equals(movie.release) : movie.release != null) return false;
        if (directors != null ? !directors.equals(movie.directors) : movie.directors != null) return false;
        if (actors != null ? !actors.equals(movie.actors) : movie.actors != null) return false;
        return owner != null ? owner.equals(movie.owner) : movie.owner == null;
    }

    public List<Person> getDirectors() {
        return directors;
    }

    public List<Person> getActors() {
        return actors;
    }

    public List<Person>getPeople() {
        List<Person> result = new ArrayList<>();
        result.addAll(directors);
        result.addAll(actors);
        return result;
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

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (release != null ? release.hashCode() : 0);
        result = 31 * result + (directors != null ? directors.hashCode() : 0);
        result = 31 * result + (actors != null ? actors.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (legal ? 1 : 0);
        return result;
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

    public void setId(long id) {
        this.id = id;
    }

    public boolean getLegality() {
        return legal;
    }
}
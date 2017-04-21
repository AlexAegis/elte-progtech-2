package com.alexaegis.progtech.database;

import com.alexaegis.progtech.misc.PersonException;
import com.alexaegis.progtech.models.movies.Movie;
import com.alexaegis.progtech.models.people.Person;
import com.alexaegis.progtech.models.people.PersonFactory;

import java.sql.Date;
import java.util.Vector;
import java.util.stream.Collectors;

public class MovieCache extends Cache {

    private Vector<Movie> movies = new Vector<>();
    private Vector<String> columnNames = new Vector<>();

    public MovieCache(Connector connector) {
        super(connector);
        columnNames.add("id");
        columnNames.add("Name");
        columnNames.add("Year");
        columnNames.add("Directors");
        columnNames.add("Actors");
        this.update();
    }

    @Override
    public Vector<Vector<Object>> getData() {
        return movies.stream().map(Movie::getData).collect(Collectors.toCollection(Vector::new));
    }

    @Override
    public Vector<String> getColumnNames() {
        return columnNames;
    }

    @Override
    public void update() {
        setQuery("SELECT * FROM MOVIES");
        super.update();
        Vector<Movie> movies = new Vector<>();
        for(int i = 0; i < super.getData().size(); i++) {
            try {
                Movie movie = new Movie((long) super.getData().get(i).get(0),
                        (String) super.getData().get(i).get(1),
                        (Date) super.getData().get(i).get(2));
                setQuery("SELECT PEOPLE.ID, PEOPLE.NAME FROM PEOPLE\n" +
                "JOIN MOVIES_PEOPLE ON MOVIES_PEOPLE.PERSON_ID = PEOPLE.ID\n" +
                "WHERE MOVIES_PEOPLE.MOVIE_ID = " + (i + 1) + " AND PEOPLE.TYPE = 'director'");
                super.update();
                Vector<Person> directors = new Vector<>();
                for(int j = 0; j < super.getData().size(); j++) {
                    directors.add(PersonFactory.createDirector(
                            (long) super.getData().get(j).get(0),
                            (String) super.getData().get(j).get(1)));
                }
                directors.forEach(movie::addPerson);
                setQuery("SELECT PEOPLE.ID, PEOPLE.NAME FROM PEOPLE\n" +
                        "JOIN MOVIES_PEOPLE ON MOVIES_PEOPLE.PERSON_ID = PEOPLE.ID\n" +
                        "WHERE MOVIES_PEOPLE.MOVIE_ID = " + (i + 1) + " AND PEOPLE.TYPE = 'actor'");
                super.update();
                Vector<Person> actors = new Vector<>();
                for(int j = 0; j < super.getData().size(); j++) {
                    actors.add(PersonFactory.createActor(
                            (long) super.getData().get(j).get(0),
                            (String) super.getData().get(j).get(1)));
                }
                actors.forEach(movie::addPerson);
                movies.add(movie);
            } catch (ClassCastException e) {
                System.out.println("Failed to parse movie");
                e.printStackTrace();
            } catch (PersonException e) {
                e.printStackTrace();
            } finally {
                setQuery("SELECT * FROM movies");
                super.update();
            }
        }
        this.movies = movies;
    }
}
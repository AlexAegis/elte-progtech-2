package com.alexaegis.progtech.database;

import com.alexaegis.progtech.models.Movie;
import com.alexaegis.progtech.models.Person;

import java.sql.Date;
import java.util.Vector;
import java.util.stream.Collectors;

public class MovieCache extends Cache {

    private Vector<Movie> movies;
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
        setQuery("SELECT * FROM movies");
        super.update();
        Vector<Movie> movies = new Vector<>();
        for(int i = 0; i < super.getData().size(); i++) {
            try {
                Movie movie = new Movie((int) super.getData().get(i).get(0),
                        (String) super.getData().get(i).get(1),
                        (Date) super.getData().get(i).get(2));

                setQuery("SELECT directors.id, directors.Name, directors.Birth FROM directors\n" +
                        "JOIN movies_directors ON movies_directors.director_id = directors.id\n" +
                        "WHERE movies_directors.movie_id = " + (i + 1));
                super.update();
                Vector<Person> directors = new Vector<>();
                for(int j = 0; j < super.getData().size(); j++) {
                    directors.add(new Person((int) super.getData().get(j).get(0),
                            (String) super.getData().get(j).get(1),
                            (Date) super.getData().get(j).get(2),
                            PersonTypes.DIRECTOR));
                }
                directors.forEach(movie::addDirector);

                setQuery("SELECT actors.id, actors.Name, actors.Birth FROM actors\n" +
                        "JOIN movies_actors ON movies_actors.actor_id = actors.id\n" +
                        "WHERE movies_actors.movie_id = " + (i + 1));
                super.update();
                Vector<Person> actors = new Vector<>();
                for(int j = 0; j < super.getData().size(); j++) {
                    actors.add(new Person((int) super.getData().get(j).get(0),
                            (String) super.getData().get(j).get(1),
                            (Date) super.getData().get(j).get(2),
                            PersonTypes.ACTOR));
                }
                actors.forEach(movie::addActor);
                movies.add(movie);
            } catch (ClassCastException e) {
                System.out.println("Failed to parse movie");
                e.printStackTrace();
            } finally {
                setQuery("SELECT * FROM movies");
                super.update();
            }
        }
        this.movies = movies;
    }
}
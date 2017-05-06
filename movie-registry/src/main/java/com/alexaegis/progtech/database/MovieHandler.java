package com.alexaegis.progtech.database;

import com.alexaegis.progtech.logic.Updatable;
import com.alexaegis.progtech.misc.IllegalLeaseException;
import com.alexaegis.progtech.misc.IllegalMovieException;
import com.alexaegis.progtech.misc.IllegalPersonException;
import com.alexaegis.progtech.misc.IllegalRelationException;
import com.alexaegis.progtech.models.movies.Movie;
import com.alexaegis.progtech.models.people.Person;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MovieHandler implements Updatable {

    private MovieCache movieCache;
    private Cache leaseCache;

    List<String> queries;

    public MovieHandler(Connector connector) {
        movieCache = new MovieCache(connector);
        leaseCache = new Cache(connector);
        leaseCache.setQuery("SELECT * FROM APP.LEASES");
        leaseCache.update();
        queries = new ArrayList<>();
        update();
    }

    public void removeMovie(Movie movie) throws IllegalMovieException {
        boolean movieFound = false;
        for (Movie movie1 : movieCache.getMovies()) {
            if(movie1.getTitle().equals(movie.getTitle())) {
                movieFound = true;
            }
        }
        if(!movieFound) {
            throw new IllegalMovieException("Movie can't be deleted, it does not exists");
        }
        try (PreparedStatement statement = movieCache.getConnector().getConnection()
                .prepareStatement("DELETE FROM APP.MOVIES WHERE ID = " + movie.getId())) {
            int affectedRows = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            update();
            movie.getPeople().forEach(person -> {
                System.out.println(person.toString());
            });
        }
    }

    public void evaluateNewMovie(Movie movie) throws IllegalMovieException {
        try (PreparedStatement statement = movieCache.getConnector().getConnection()
                .prepareStatement("INSERT INTO APP.MOVIES (TITLE, RELEASE, ORIGINAL) VALUES (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, movie.getTitle());
            statement.setDate(2, movie.getRelease());
            statement.setBoolean(3, movie.getLegality());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new IllegalMovieException("Creating movie failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    movie.setId(generatedKeys.getLong(1));
                }
                else {
                    throw new IllegalMovieException("Creating movie failed, no ID obtained.");
                }
            }

            for (Person person : movie.getPeople()) {
                try {
                    evaluateNewPerson(person, movie);
                } catch (IllegalPersonException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            update();
            movie.getPeople().forEach(person -> {
                System.out.println(person.toString());
            });
        }
    }

    private void evaluateNewPerson(Person person, Movie movie) throws IllegalPersonException {
        try (PreparedStatement statement = movieCache.getConnector().getConnection().prepareStatement("INSERT INTO APP.PEOPLE (NAME, TYPE) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, person.getName().toString());
            statement.setString(2, person.getType().toString().toLowerCase());

            if(!movieCache.getPeople().contains(person)) {
                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new IllegalPersonException("Creating person failed, no rows affected.");
                }

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        person.setId(generatedKeys.getLong(1));
                    }
                    else {
                        throw new IllegalPersonException("Creating person failed, no ID obtained.");
                    }
                }
                update();
                try {
                    evaluateNewRelation(person, movie);
                } catch (IllegalRelationException e) {
                    e.printStackTrace();
                }
            } else {
                for (Person person1 : movieCache.getPeople()) {
                    if (person1.equals(person)) {
                        update();
                        try {
                            evaluateNewRelation(person1, movie);
                        } catch (IllegalRelationException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }

            }



        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            update();
        }
    }

    private void evaluateNewRelation(Person person, Movie movie) throws IllegalRelationException {
        Map<Long, Long> relations = movieCache.getRelations();
        System.out.println("movie.getId()" + movie.getId());
        System.out.println("relations.get(movie.getId())" + relations.get(movie.getId()));
        System.out.println("person.getId()" + person.getId());
        if(relations.containsKey(movie.getId()) && relations.get(movie.getId()) != null && relations.get(movie.getId()) == person.getId()) {
            System.out.println("LOL");
            throw new IllegalRelationException(person.getName() + " is already in the database");
        }
        try (PreparedStatement statement = movieCache.getConnector().getConnection().prepareStatement("INSERT INTO APP.MOVIES_PEOPLE (MOVIE_ID, PERSON_ID) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, movie.getId());
            statement.setLong(2, person.getId());
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new IllegalRelationException("Creating relation failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            update();
        }
    }

    @Override
    public void update() {
        movieCache.update();
    }

    public void panic() {
        for (Movie movie : movieCache.getMovies()) {
            if(!movie.getLegality()) {
                try {
                    removeMovie(movie);
                } catch (IllegalMovieException e) {
                    e.printStackTrace();
                }
            }
        }
        update();
    }

    public void evaluateNewLease(Movie movie, String leaser, java.sql.Date date) throws IllegalLeaseException {
        if (leaseCache.getColumn(1).contains(movie.getId())) {
            JOptionPane.showMessageDialog(null, "This movie is already leased!");
            throw new IllegalLeaseException("Movie already leased!");
        }
        try (PreparedStatement statement = movieCache.getConnector().getConnection().prepareStatement("INSERT INTO APP.LEASES(MOVIE_ID, NAME, DATE) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, movie.getId());
            statement.setString(2, leaser);
            statement.setDate(3, date);
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new IllegalLeaseException("Creating lease failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            update();
        }
    }

    public void removeLease(long movieId) throws IllegalLeaseException {
        try (PreparedStatement statement = movieCache.getConnector().getConnection().prepareStatement("DELETE FROM APP.LEASES WHERE MOVIE_ID = " + movieId)) {
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new IllegalLeaseException("Deleting lease failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            update();
        }
    }
}

package com.alexaegis.progtech.database;

import com.alexaegis.progtech.misc.IllegalMovieException;
import com.alexaegis.progtech.models.Movie;
import com.alexaegis.progtech.models.people.Person;
import com.alexaegis.progtech.models.people.PersonTypes;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

public class MovieInserter {

    private Connector connector;
    private Cache moviesCache;
    private Cache actorsCache;
    private Cache directorsCache;
    private Cache moviesDirectorsCache;
    private Cache moviesActorsCache;

    private List<Integer> movieIds;
    private List<Integer> actorsIds;
    private List<Integer> directorsIds;
    private List<String> movieTitles;
    private List<String> actorsNames;
    private List<String> directorsNames;
    private List<Integer> moviesDirectorsMovieIds;
    private List<Integer> moviesDirectorsDirectorIds;
    private List<Integer> moviesActorsMovieIds;
    private List<Integer> moviesActorsActorIds;

    List<String> queries = new ArrayList<>();

    public MovieInserter(Connector connector) {
        this.connector = connector;
    }

    public void evaluateNewMovie(Movie movie) throws IllegalMovieException {
        moviesCache = new Cache(connector);
        actorsCache = new Cache(connector);
        directorsCache = new Cache(connector);
        moviesDirectorsCache = new Cache(connector);
        moviesActorsCache = new Cache(connector);
        moviesCache.setQuery("select * from movies");
        actorsCache.setQuery("select * from actors");
        directorsCache.setQuery("select * from directors");
        moviesDirectorsCache.setQuery("select * from movies_directors");
        moviesActorsCache.setQuery("select * from movies_actors");
        updateCache();

        int movieId = -1;

        if(movieTitles.contains(movie.getTitle())) {
            throw new IllegalMovieException();
        } else {
            List<Integer> takenIds = moviesCache.getData().stream().map(objects -> objects.get(0)).map(o -> (Integer)o).collect(Collectors.toList());
            for (int i = 1; i <= takenIds.size() + 1; i++) {
                if(!takenIds.contains(i)) {
                    movieId = i;
                    break;
                }
            }
        }
        for (Person person : movie.getActors()) {
            evaluateNewPerson(person, movieId);
        }
        for (Person person : movie.getDirectors()) {
            evaluateNewPerson(person, movieId);
        }
        queries.add("INSERT INTO movies VALUES (" + movieId + ", \"" + movie.getTitle() + "\", \"" + movie.getRelease() + "\")");
        queries.add("COMMIT");
        queries.forEach(System.out::println);
        queries.forEach(this::executeQuery);
    }

    private void updateCache() {
        moviesCache.update();
        actorsCache.update();
        directorsCache.update();
        moviesDirectorsCache.update();
        moviesActorsCache.update();

        movieIds = moviesCache.getColumn(0).stream().map(o -> (Integer)o).collect(Collectors.toList());
        actorsIds = actorsCache.getColumn(0).stream().map(o -> (Integer)o).collect(Collectors.toList());
        directorsIds = directorsCache.getColumn(0).stream().map(o -> (Integer)o).collect(Collectors.toList());
        movieTitles = moviesCache.getColumn(1).stream().map(o -> (String)o).collect(Collectors.toList());
        actorsNames = actorsCache.getColumn(1).stream().map(o -> (String)o).collect(Collectors.toList());
        directorsNames = directorsCache.getColumn(1).stream().map(o -> (String)o).collect(Collectors.toList());
        moviesDirectorsMovieIds = moviesDirectorsCache.getColumn(0).stream().map(o -> (Integer)o).collect(Collectors.toList());
        moviesDirectorsDirectorIds = moviesDirectorsCache.getColumn(1).stream().map(o -> (Integer)o).collect(Collectors.toList());
        moviesActorsMovieIds = moviesActorsCache.getColumn(0).stream().map(o -> (Integer)o).collect(Collectors.toList());
        moviesActorsActorIds = moviesActorsCache.getColumn(1).stream().map(o -> (Integer)o).collect(Collectors.toList());
    }

    private void evaluateNewPerson(Person person, int movieId) {
        updateCache();
        List<String> names;
        List<Integer> ids;
        if(person.getType().equals(PersonTypes.ACTOR)) {
            names = actorsNames;
            ids = actorsIds;
        } else {
            names = directorsNames;
            ids = directorsIds;
        }

        int personId = -1;
        boolean containsPerson = false;
        if(names.contains(person.getName())) {
            personId = ids.get(names.indexOf(person.getName()));
            containsPerson = true;
        } else {
            for (int i = 1; i <= ids.size() + 1; i++) {
                if(!ids.contains(i)) {
                    personId = i;
                    break;
                }
            }
        }
        person.setId(personId);

        if(!containsPerson) {
            addNewPerson(person, movieId);
            //ids.add(personId);
            //names.add(person.getName());
        } else {
            evaluateNewMoviePersonRelation(person, movieId);
        }
    }

    private void addNewPerson(Person person, int movieId) {
        queries.add("INSERT INTO " + (person.getType().equals(PersonTypes.ACTOR) ? "actors" : "directors") + " VALUES (" + person.getId() + ", \"" + person.getName() + "\", " + person.getBirth() + ");");
        queries.add("COMMIT;");
        addNewMoviePersonRelation(person, movieId);
    }

    private void evaluateNewMoviePersonRelation(Person person, int movieId) {
        updateCache();
        List<Integer> movieIds;
        List<Integer> personIds;
        if(person.getType().equals(PersonTypes.ACTOR)) {
            movieIds = moviesActorsMovieIds;
            personIds = moviesActorsActorIds;
        } else {
            movieIds = moviesDirectorsMovieIds;
            personIds = moviesDirectorsDirectorIds;
        }
        boolean present = false;
        for (int i = 0; i < movieIds.size(); i++)
            if(movieIds.get(i).equals(movieId) && personIds.get(i).equals(person.getId()))
                present = true;
        if(!present) {
            addNewMoviePersonRelation(person, movieId);
            movieIds.add(movieId);
            //personIds.add(person.getId());
        }
    }

    private void addNewMoviePersonRelation(Person person, int movieId) {
        queries.add("INSERT INTO movies_" + (person.getType().equals(PersonTypes.ACTOR) ? "actors" : "directors") + " VALUES (" + movieId + ", " + person.getId() + ");");
        queries.add("COMMIT;");
    }

    public void executeQuery(String query) {
        try(Statement statement = connector.getConnection().createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
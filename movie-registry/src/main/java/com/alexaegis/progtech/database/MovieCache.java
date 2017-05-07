package com.alexaegis.progtech.database;

import com.alexaegis.progtech.misc.IllegalPersonException;
import com.alexaegis.progtech.models.movies.Movie;
import com.alexaegis.progtech.models.people.Person;
import com.alexaegis.progtech.models.people.PersonFactory;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

public class MovieCache extends Cache {

    private Vector<Movie> movies = new Vector<>();
    private Vector<String> columnNames = new Vector<>();
    private Cache relationsTable;
    private Cache peopleTable;

    public MovieCache(Connector connector) {
        super(connector);
        relationsTable = new Cache(connector);
        relationsTable.setQuery("SELECT MOVIE_ID, PERSON_ID FROM MOVIES_PEOPLE");
        relationsTable.update();
        peopleTable = new Cache(connector);
        peopleTable.setQuery("SELECT PEOPLE.ID, PEOPLE.NAME, PEOPLE.TYPE FROM PEOPLE");
        peopleTable.update();

        columnNames.add("id");
        columnNames.add("Name");
        columnNames.add("Year");
        columnNames.add("Directors");
        columnNames.add("Actors");
        columnNames.add("Originality");
        this.update();
    }

    @Override
    public Vector<Vector<Object>> getData() {
        return movies.stream().map(Movie::getData).collect(Collectors.toCollection(Vector::new));
    }

    public Vector<Movie> getMovies() {
        return movies;
    }

    public List<Person> getPeople() {
        return movies.stream().flatMap(movie -> movie.getPeople().stream()).collect(Collectors.toList());
    }

    public Map<Long, Long> getRelations() {
        relationsTable.update();
        Map<Long, Long> longLongMap = new HashMap<>();
        for(Vector<Object> vector : relationsTable.getData()) {
            longLongMap.put((long) vector.get(0), (long) vector.get(1));
        }
        return longLongMap;
    }

    @Override
    public Vector<String> getColumnNames() {
        return columnNames;
    }

    @Override
    public void update() {
        setQuery("SELECT * FROM MOVIES");
        super.update();
        relationsTable.update();
        Vector<Movie> movies = new Vector<>();
        int movieCount = super.getData().size();
        for(int i = 0; i < movieCount; i++) {
            try {
                System.out.println((int) super.getData().get(i).get(3));
                Movie movie = new Movie((long) super.getData().get(i).get(0),
                        (String) super.getData().get(i).get(1),
                        (Date) super.getData().get(i).get(2),
                        ((int) super.getData().get(i).get(3)) == 1);
                peopleTable.setQuery("SELECT PEOPLE.ID, PEOPLE.NAME, PEOPLE.TYPE FROM PEOPLE\n" +
                "JOIN MOVIES_PEOPLE ON MOVIES_PEOPLE.PERSON_ID = PEOPLE.ID\n" +
                "WHERE MOVIES_PEOPLE.MOVIE_ID = " + super.getData().get(i).get(0));
                peopleTable.update();
                relationsTable.update();
                Vector<Person> people = new Vector<>();
                for(int j = 0; j < peopleTable.getData().size(); j++) {
                    people.add(PersonFactory.createPerson(
                            (long) peopleTable.getData().get(j).get(0),
                            (String) peopleTable.getData().get(j).get(1),
                            (String) peopleTable.getData().get(j).get(2)));
                }
                people.forEach(movie::addPerson);
                movies.add(movie);
            } catch (ClassCastException e) {
                System.out.println("Failed to parse movie");
                e.printStackTrace();
            } catch (IllegalPersonException e) {
                e.printStackTrace();
            } finally {
                setQuery("SELECT * FROM movies");
                super.update();
            }
        }
        this.movies = movies;
    }
}
package com.alexaegis.progtech.cache;

import java.util.ArrayList;
import java.util.List;

public class Movie {

    private String name;
    private int year;
    private String director;
    private List<String> actors = new ArrayList<>();
    private boolean legal;

    private String owner;

    public Movie(String name, int year, String director) {
        this.name = name;
        this.year = year;
        this.director = director;
    }

}
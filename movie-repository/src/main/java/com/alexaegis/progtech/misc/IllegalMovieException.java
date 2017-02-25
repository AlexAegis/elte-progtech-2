package com.alexaegis.progtech.misc;

public class IllegalMovieException extends Exception {
    public IllegalMovieException() {
        super("Can't create movie");
    }
}

package com.alexaegis.progtech.models.movies;

import java.util.Date;

public class Lease {

    private long id;
    private String name;
    private Movie movie;
    private Date date;

    public Lease(long id, String name, Movie movie, Date date) {
        this.id = id;
        this.name = name;
        this.movie = movie;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lease lease = (Lease) o;

        if (id != lease.id) return false;
        if (name != null ? !name.equals(lease.name) : lease.name != null) return false;
        if (movie != null ? !movie.equals(lease.movie) : lease.movie != null) return false;
        return date != null ? date.equals(lease.date) : lease.date == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (movie != null ? movie.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Lease{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", movie=" + movie +
                ", date=" + date +
                '}';
    }
}
package com.movies.task_2;


import java.util.*;

public interface MovieDAO {

    void createTable();
    void dropTable();

    Movie insert(Movie movie);
    void delete(int id);
    void updateTitle(int id, String newTitle);

    Optional<Movie> findById(int id);
    List<Movie> findAll();
    List<Movie> findByTitle(String part);
    List<Movie> findByGenre(String genre);
    List<Movie> findByYear(int year);
}


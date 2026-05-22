package com.movies.task_3;

import jakarta.persistence.*;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "genre", length = 100)
    private String genre;

    @Column(name = "year")
    private Integer year;

    public Movie() {}

    public Movie(String title, String genre, int year) {
        this.title = title;
        this.genre = genre;
        this.year = year;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    @Override
    public String toString() {
        return String.format("Movie{id=%d, title='%s', genre='%s', year=%d}",
                id, title, genre, year);
    }
}


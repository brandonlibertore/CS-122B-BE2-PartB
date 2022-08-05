package com.github.klefstad_teaching.cs122b.movies.model.data;

public class MovieObjects {

    private Long id;
    private String title;
    private Integer year;
    private String director;
    private Double rating;
    private String backdropPath;
    private String posterPath;
    private Boolean hidden;

    public Long getId() {
        return id;
    }

    public MovieObjects setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public MovieObjects setTitle(String title) {
        this.title = title;
        return this;
    }

    public Integer getYear() {
        return year;
    }

    public MovieObjects setYear(Integer year) {
        this.year = year;
        return this;
    }

    public String getDirector() {
        return director;
    }

    public MovieObjects setDirector(String director) {
        this.director = director;
        return this;
    }

    public Double getRating() {
        return rating;
    }

    public MovieObjects setRating(Double rating) {
        this.rating = rating;
        return this;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public MovieObjects setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
        return this;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public MovieObjects setPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public MovieObjects setHidden(Boolean hidden) {
        this.hidden = hidden;
        return this;
    }
}

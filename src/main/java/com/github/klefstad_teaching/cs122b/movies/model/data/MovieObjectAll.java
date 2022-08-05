package com.github.klefstad_teaching.cs122b.movies.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

public class MovieObjectAll {

    private Long id;
    private String title;
    private Integer year;
    private String director;
    private Double rating;
    private Integer num_votes;
    private BigInteger budget;
    private BigInteger revenue;
    private String overview;
    private String backdropPath;
    private String posterPath;
    private Boolean hidden;

    public Long getId() {
        return id;
    }

    public MovieObjectAll setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public MovieObjectAll setTitle(String title) {
        this.title = title;
        return this;
    }

    public Integer getYear() {
        return year;
    }

    public MovieObjectAll setYear(Integer year) {
        this.year = year;
        return this;
    }

    public String getDirector() {
        return director;
    }

    public MovieObjectAll setDirector(String director) {
        this.director = director;
        return this;
    }

    public Double getRating() {
        return rating;
    }

    public MovieObjectAll setRating(Double rating) {
        this.rating = rating;
        return this;
    }

    @JsonProperty("numVotes")
    public Integer getNum_votes() {
        return num_votes;
    }

    public MovieObjectAll setNum_votes(Integer num_votes) {
        this.num_votes = num_votes;
        return this;
    }

    public BigInteger getBudget() {
        return budget;
    }

    public MovieObjectAll setBudget(BigInteger budget) {
        this.budget = budget;
        return this;
    }

    public BigInteger getRevenue() {
        return revenue;
    }

    public MovieObjectAll setRevenue(BigInteger revenue) {
        this.revenue = revenue;
        return this;
    }

    public String getOverview() {
        return overview;
    }

    public MovieObjectAll setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public MovieObjectAll setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
        return this;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public MovieObjectAll setPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public MovieObjectAll setHidden(Boolean hidden) {
        this.hidden = hidden;
        return this;
    }
}

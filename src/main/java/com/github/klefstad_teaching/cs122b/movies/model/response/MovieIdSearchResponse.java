package com.github.klefstad_teaching.cs122b.movies.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.klefstad_teaching.cs122b.core.result.Result;
import com.github.klefstad_teaching.cs122b.movies.model.data.GenreObjects;
import com.github.klefstad_teaching.cs122b.movies.model.data.MovieObjectAll;
import com.github.klefstad_teaching.cs122b.movies.model.data.PersonObjects;

import java.util.List;

public class MovieIdSearchResponse {

    private Result result;
    private MovieObjectAll movies;
    private List<GenreObjects> genres;
    private List<PersonObjects> persons;

    public Result getResult() {
        return result;
    }

    public MovieIdSearchResponse setResult(Result result) {
        this.result = result;
        return this;
    }

    @JsonProperty("movie")
    public MovieObjectAll getMovies() {
        return movies;
    }

    public MovieIdSearchResponse setMovies(MovieObjectAll movies) {
        this.movies = movies;
        return this;
    }

    public List<GenreObjects> getGenres() {
        return genres;
    }

    public MovieIdSearchResponse setGenres(List<GenreObjects> genres) {
        this.genres = genres;
        return this;
    }

    public List<PersonObjects> getPersons() {
        return persons;
    }

    public MovieIdSearchResponse setPersons(List<PersonObjects> persons) {
        this.persons = persons;
        return this;
    }
}

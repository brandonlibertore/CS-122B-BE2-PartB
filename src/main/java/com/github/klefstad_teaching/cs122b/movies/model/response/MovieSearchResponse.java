package com.github.klefstad_teaching.cs122b.movies.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.klefstad_teaching.cs122b.core.result.Result;
import com.github.klefstad_teaching.cs122b.movies.model.data.MovieObjects;

import java.util.List;

public class MovieSearchResponse {

    private Result result;
    private List<MovieObjects> movies;

    public Result getResult() {
        return result;
    }

    public MovieSearchResponse setResult(Result result) {
        this.result = result;
        return this;
    }

    @JsonProperty("movies")
    public List<MovieObjects> getMovies() {
        return movies;
    }

    public MovieSearchResponse setMovies(List<MovieObjects> movies) {
        this.movies = movies;
        return this;
    }
}

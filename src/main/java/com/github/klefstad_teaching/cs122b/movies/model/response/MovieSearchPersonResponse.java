package com.github.klefstad_teaching.cs122b.movies.model.response;

import com.github.klefstad_teaching.cs122b.core.result.Result;
import com.github.klefstad_teaching.cs122b.movies.model.data.MovieObjects;

import java.util.List;

public class MovieSearchPersonResponse {

    private Result result;
    private List<MovieObjects> movies;

    public Result getResult() {
        return result;
    }

    public MovieSearchPersonResponse setResult(Result result) {
        this.result = result;
        return this;
    }

    public List<MovieObjects> getMovies() {
        return movies;
    }

    public MovieSearchPersonResponse setMovies(List<MovieObjects> movies) {
        this.movies = movies;
        return this;
    }
}

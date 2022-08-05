package com.github.klefstad_teaching.cs122b.movies.model.response;

import com.github.klefstad_teaching.cs122b.core.result.Result;
import com.github.klefstad_teaching.cs122b.movies.model.data.PersonObjectAll;

import java.util.List;

public class MoviePersonSearchResponse {

    private Result result;
    private List<PersonObjectAll> persons;

    public Result getResult() {
        return result;
    }

    public MoviePersonSearchResponse setResult(Result result) {
        this.result = result;
        return this;
    }

    public List<PersonObjectAll> getPersons() {
        return persons;
    }

    public MoviePersonSearchResponse setPersons(List<PersonObjectAll> persons) {
        this.persons = persons;
        return this;
    }
}

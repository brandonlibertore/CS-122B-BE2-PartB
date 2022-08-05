package com.github.klefstad_teaching.cs122b.movies.model.response;

import com.github.klefstad_teaching.cs122b.core.result.Result;
import com.github.klefstad_teaching.cs122b.movies.model.data.PersonObjectAll;

public class PersonIdResponse {

    private Result result;
    private PersonObjectAll person;

    public Result getResult() {
        return result;
    }

    public PersonIdResponse setResult(Result result) {
        this.result = result;
        return this;
    }

    public PersonObjectAll getPerson() {
        return person;
    }

    public PersonIdResponse setPerson(PersonObjectAll person) {
        this.person = person;
        return this;
    }
}

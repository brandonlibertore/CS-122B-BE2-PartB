package com.github.klefstad_teaching.cs122b.movies.model.data;

public class PersonObjects {

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public PersonObjects setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public PersonObjects setName(String name) {
        this.name = name;
        return this;
    }
}

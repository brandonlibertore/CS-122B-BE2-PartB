package com.github.klefstad_teaching.cs122b.movies.model.data;

import java.sql.Date;

public class PersonObjectAll {

    private Integer id;
    private String name;
    private Date birthday;
    private String biography;
    private String birthplace;
    private Float popularity;
    private String profilePath;

    public Integer getId() {
        return id;
    }

    public PersonObjectAll setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public PersonObjectAll setName(String name) {
        this.name = name;
        return this;
    }

    public Date getBirthday() {
        return birthday;
    }

    public PersonObjectAll setBirthday(Date birthday) {
        this.birthday = birthday;
        return this;
    }

    public String getBiography() {
        return biography;
    }

    public PersonObjectAll setBiography(String biography) {
        this.biography = biography;
        return this;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public PersonObjectAll setBirthplace(String birthplace) {
        this.birthplace = birthplace;
        return this;
    }

    public Float getPopularity() {
        return popularity;
    }

    public PersonObjectAll setPopularity(Float popularity) {
        this.popularity = popularity;
        return this;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public PersonObjectAll setProfilePath(String profilePath) {
        this.profilePath = profilePath;
        return this;
    }
}

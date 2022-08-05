package com.github.klefstad_teaching.cs122b.movies.repo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;
import com.github.klefstad_teaching.cs122b.movies.model.data.*;
import com.github.klefstad_teaching.cs122b.movies.model.response.MovieIdSearchResponse;
import com.github.klefstad_teaching.cs122b.movies.model.response.MovieSearchPersonResponse;
import com.github.klefstad_teaching.cs122b.movies.model.response.MovieSearchResponse;
import com.mysql.cj.protocol.Resultset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MovieRepo
{

    private ObjectMapper objectMapper;
    private NamedParameterJdbcTemplate template;

    @Autowired
    public MovieRepo(ObjectMapper objectMapper, NamedParameterJdbcTemplate template)
    {
        this.objectMapper = objectMapper;
        this.template = template;
    }

    public List<MovieObjects> searchMovie(String sql, MapSqlParameterSource source){
        List<MovieObjects> movies = this.template.query(sql,
                source, (rs, rowNum) ->
                        new MovieObjects()
                                .setId(rs.getLong("id"))
                                .setTitle(rs.getString("title"))
                                .setYear(rs.getInt("year"))
                                .setDirector(rs.getString("name"))
                                .setRating(rs.getDouble("rating"))
                                .setBackdropPath(rs.getString("backdrop_path"))
                                .setPosterPath(rs.getString("poster_path"))
                                .setHidden(rs.getBoolean("hidden")));
        return movies;
    }

    public List<MovieObjectAll> movieExist(String sql, MapSqlParameterSource source){
        return this.template.query(sql,
                source, (rs, rowNum) ->
                new MovieObjectAll()
                        .setId(rs.getLong("id"))
                        .setTitle(rs.getString("title"))
                        .setYear(rs.getInt("year"))
                        .setDirector(rs.getString("name"))
                        .setRating(rs.getDouble("rating"))
                        .setNum_votes(rs.getInt("num_votes"))
                        .setBudget(BigInteger.valueOf(rs.getLong("budget")))
                        .setRevenue(BigInteger.valueOf(rs.getLong("revenue")))
                        .setOverview(rs.getString("overview"))
                        .setBackdropPath(rs.getString("backdrop_path"))
                        .setPosterPath(rs.getString("poster_path"))
                        .setHidden(rs.getBoolean("hidden")));
    }

    public List<GenreObjects> movieGenre(String sql, MapSqlParameterSource source){
        return this.template.query(sql,
                source, (rs, rowNum) ->
                        new GenreObjects()
                                .setId(rs.getInt("id"))
                                .setName(rs.getString("name")));
    }

    public List<PersonObjects> moviePerson(String sql, MapSqlParameterSource source){
        return this.template.query(sql,
                source, (rs, rowNum) ->
                new PersonObjects()
                        .setId(rs.getInt("person_id"))
                        .setName(rs.getString("name")));
    }

    public List<PersonObjectAll> searchPerson(String sql, MapSqlParameterSource source){
        return this.template.query(sql,
                source, (rs, rowNum) ->
                new PersonObjectAll()
                        .setId(rs.getInt("id"))
                        .setName(rs.getString("name"))
                        .setBirthday(rs.getDate("birthday"))
                        .setBiography(rs.getString("biography"))
                        .setBirthplace(rs.getString("birthplace"))
                        .setPopularity(rs.getFloat("popularity"))
                        .setProfilePath(rs.getString("profile_path")));
    }
}

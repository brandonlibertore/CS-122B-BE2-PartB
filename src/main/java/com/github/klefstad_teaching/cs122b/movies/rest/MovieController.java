package com.github.klefstad_teaching.cs122b.movies.rest;

import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;
import com.github.klefstad_teaching.cs122b.core.security.JWTManager;
import com.github.klefstad_teaching.cs122b.movies.model.data.*;
import com.github.klefstad_teaching.cs122b.movies.model.response.*;
import com.github.klefstad_teaching.cs122b.movies.repo.MovieRepo;
import com.github.klefstad_teaching.cs122b.movies.util.Validate;
import com.nimbusds.jwt.SignedJWT;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
public class MovieController
{
    private final MovieRepo repo;
    private final Validate validate;

    @Autowired
    public MovieController(MovieRepo repo, Validate validate)
    {
        this.repo = repo;
        this.validate = validate;
    }

    @GetMapping("/movie/search")
    public ResponseEntity<MovieSearchResponse> movieSearch(@AuthenticationPrincipal SignedJWT user,
                                                           @RequestParam Optional<String> title, @RequestParam Optional<Integer> year,
                                                           @RequestParam Optional<String> director, @RequestParam Optional<String> genre,
                                                           @RequestParam Optional<Integer> limit, @RequestParam Optional<Integer> page,
                                                           @RequestParam Optional<String> orderBy, @RequestParam Optional<String> direction)
    {

        // Validate the data entries:
        validate.validityCheck(limit, page, orderBy, direction);

        // Initialize arguments to be passed into template.query:
        StringBuilder         sql;
        MapSqlParameterSource source     = new MapSqlParameterSource();
        boolean               whereAdded = false;

        // Begin creating sql statement:

        //User is querying with director and genre selected.
        final String MOVIE_WITH_GENRE =
                "SELECT m.id, m.title, m.year, p.name, m.rating, m.backdrop_path, m.poster_path, m.hidden " +
                        "FROM movies.movie m " +
                        "   JOIN movies.person p ON m.director_id = p.id " +
                        "   JOIN movies.movie_genre mg ON m.id = mg.movie_id " +
                        "   JOIN movies.genre g ON mg.genre_id = g.id ";

        final String MOVIE_NO_GENRE =
                "SELECT m.id, m.title, m.year, p.name, m.rating, m.backdrop_path, m.poster_path, m.hidden " +
                        "FROM movies.movie m " +
                        "   JOIN movies.person p ON m.director_id = p.id ";

        // Query to get list of movies that fit the parameters:

        // First Check if genre is present so that we can join our tables
        // movies.movie, movies.movie_genre, movies.genre, movies.person:

        // If Genre is present:
        if (genre.isPresent()){
            sql = new StringBuilder(MOVIE_WITH_GENRE);
            sql.append(" WHERE g.name LIKE :genre ");
            String wildcardSearch = '%' + genre.get() + '%';
            source.addValue("genre", wildcardSearch, Types.VARCHAR);
            whereAdded = true;
        }
        // If Genre is not present:
        else{
            sql = new StringBuilder(MOVIE_NO_GENRE);
        }

        // BEGIN TO ADD WHERE STATEMENTS:

        // Check if user is searching for title:
        if (title.isPresent()){
            if (whereAdded){
                sql.append(" AND ");
            }
            else{
                sql.append(" WHERE ");
                whereAdded = true;
            }
            sql.append(" m.title LIKE :title ");
            String wildcardSearch = '%' + title.get() + '%';
            source.addValue("title", wildcardSearch, Types.VARCHAR);
        }

        // Check if user is searching for year:
        if (year.isPresent()){
            if (whereAdded){
                sql.append(" AND ");
            }
            else{
                sql.append(" WHERE ");
                whereAdded = true;
            }
            sql.append(" m.year = :year ");
            source.addValue("year", year.get(), Types.INTEGER);
        }

        // Check if user is searching for director:
        if (director.isPresent()){
            if (whereAdded){
                sql.append(" AND ");
            }
            else{
                sql.append(" WHERE ");
                whereAdded = true;
            }
            sql.append(" p.name LIKE :director ");
            String wildcardSearch = '%' + director.get() + '%';
            source.addValue("director", wildcardSearch, Types.VARCHAR);
        }

        // Check if user is searching for genre:
        if (genre.isPresent()){
            if (whereAdded){
                sql.append(" AND ");
            }
            else{
                sql.append(" WHERE ");
                whereAdded = true;
            }
            sql.append(" g.name LIKE :genre ");
            String wildcardSearch = '%' + genre.get() + '%';
            source.addValue("genre", wildcardSearch, Types.VARCHAR);
        }

        // Check if user is either admin/employee or regular user account:
        try{
            List<String> roles = user.getJWTClaimsSet().getStringListClaim(JWTManager.CLAIM_ROLES);
            if (whereAdded){
                sql.append(" AND ");
            }
            else{
                sql.append(" WHERE ");
                whereAdded = true;
            }
            if (roles.size() > 0){
                if (roles.contains("ADMIN") | roles.contains("EMPLOYEE")){
                    sql.append(" (m.hidden = false OR m.hidden = true) ");
                }
                else{
                    sql.append(" m.hidden = false ");
                }
            }
            else{
                sql.append(" m.hidden = false ");
            }
        }
        catch (Exception e){
            sql.append(" m.hidden = false");
        }

        // Create our order by sql statement:
        SearchOrderBy orderByString = SearchOrderBy.fromString(orderBy, direction);
        sql.append(orderByString.toSql());

        // If limit is given:
        if (limit.isPresent()){
            sql.append(" LIMIT :limit ");
            source.addValue("limit", limit.get(), Types.INTEGER);
        }

        // If limit and page are given:
        if (limit.isPresent() && page.isPresent()){
            sql.append(" OFFSET :page ");
            source.addValue("page", ((page.get() - 1) * limit.get()), Types.INTEGER);
        }

        // If limit is not given, but page is give:
        if (!limit.isPresent() && page.isPresent()){
            sql.append(" LIMIT 10 OFFSET :page");
            source.addValue("page", ((page.get() - 1) * 10), Types.INTEGER);
        }

        // If limit and page are both not given:
        if (!limit.isPresent() && !page.isPresent()){
            sql.append(" LIMIT 10 OFFSET 0");
        }

        //Create the list of movies objects:
        List<MovieObjects> movies = repo.searchMovie(sql.toString(), source);

        // If our movie list is empty throw that no movies were found:
        if (movies.size() < 1){
            throw new ResultError(MoviesResults.NO_MOVIES_FOUND_WITHIN_SEARCH);
        }

        // Create response for movies found:
        MovieSearchResponse body = new MovieSearchResponse()
                .setResult(MoviesResults.MOVIES_FOUND_WITHIN_SEARCH)
                .setMovies(movies);

        // Return our response:
        return ResponseEntity
                .status(body.getResult().status())
                .body(body);
    }

    @GetMapping("/movie/search/person/{personId}")
    public ResponseEntity<MovieSearchPersonResponse> movieSearchPerson(@AuthenticationPrincipal SignedJWT user,
                                                                       @PathVariable Long personId,
                                                                       @RequestParam Optional<Integer> limit,
                                                                       @RequestParam Optional<Integer> page,
                                                                       @RequestParam Optional<String> orderBy,
                                                                       @RequestParam Optional<String> direction)
    {
        try{
            // Check that given query items are valid:
            validate.validityCheck(limit, page, orderBy, direction);

            // Initialize arguments to be passed into template.query:
            StringBuilder         sql;
            MapSqlParameterSource source     = new MapSqlParameterSource();
            boolean               whereAdded = false;

            // Create the sql statement:
            final String MOVIE_PERSON =
                    "SELECT m.id, m.title, m.year, p.name, m.rating, m.backdrop_path, m.poster_path, m.hidden " +
                            "FROM movies.movie m " +
                            " JOIN movies.person p on p.id = m.director_id " +
                            " JOIN movies.movie_person mp ON m.id = mp.movie_id " +
                            " WHERE mp.person_id = :personId ";

            sql = new StringBuilder(MOVIE_PERSON);
            source.addValue("personId", personId, Types.INTEGER);
            whereAdded = true;

            // Check if user is either admin/employee or regular user account:
            try{
                List<String> roles = user.getJWTClaimsSet().getStringListClaim(JWTManager.CLAIM_ROLES);
                if (whereAdded){
                    sql.append(" AND ");
                }
                else{
                    sql.append(" WHERE ");
                    whereAdded = true;
                }
                if (roles.size() > 0){
                    if (roles.contains("ADMIN") | roles.contains("EMPLOYEE")){
                        sql.append(" (m.hidden = false OR m.hidden = true) ");
                    }
                    else{
                        sql.append(" m.hidden = false ");
                    }
                }
                else{
                    sql.append(" m.hidden = false ");
                }
            }
            catch (Exception e){
                sql.append(" m.hidden = false");
            }

            // Create our order by sql statement:
            SearchOrderBy orderByString = SearchOrderBy.fromString(orderBy, direction);
            sql.append(orderByString.toSql());

            // If limit is given:
            if (limit.isPresent()){
                sql.append(" LIMIT :limit ");
                source.addValue("limit", limit.get(), Types.INTEGER);
            }

            // If limit and page are given:
            if (limit.isPresent() && page.isPresent()){
                sql.append(" OFFSET :page ");
                source.addValue("page", ((page.get() - 1) * limit.get()), Types.INTEGER);
            }

            // If limit is not given, but page is give:
            if (!limit.isPresent() && page.isPresent()){
                sql.append(" LIMIT 10 OFFSET :page ");
                source.addValue("page", ((page.get() - 1) * 10), Types.INTEGER);
            }

            // If limit and page are both not given:
            if (!limit.isPresent() && !page.isPresent()){
                sql.append(" LIMIT 10 OFFSET 0 ");
            }

            List<MovieObjects> movies = this.repo.searchMovie(sql.toString(), source);

            if (movies.size() < 1){
                throw new ResultError(MoviesResults.NO_MOVIES_WITH_PERSON_ID_FOUND);
            }

            MovieSearchPersonResponse body = new MovieSearchPersonResponse()
                    .setResult(MoviesResults.MOVIES_WITH_PERSON_ID_FOUND)
                    .setMovies(movies);

            return ResponseEntity
                    .status(body.getResult().status())
                    .body(body);
        }
        catch (IllegalArgumentException e){
            throw new ResultError(MoviesResults.NO_MOVIES_WITH_PERSON_ID_FOUND);
        }
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<MovieIdSearchResponse> movieIdSearch(@AuthenticationPrincipal SignedJWT user,
                                                               @PathVariable Long movieId)
    {
        // Initialize Arguments to construct sql statement for movie object:
        StringBuilder         sqlMovie;
        MapSqlParameterSource sourceMovie     = new MapSqlParameterSource();
        boolean               whereAdded      = true;

        final String MOVIE_EXIST =
                "SELECT m.id, m.title, m.year, p.name, m.rating, m.num_votes," +
                        " m.budget, m.revenue, m.overview, m.backdrop_path, m.poster_path, m.hidden " +
                        "FROM movies.movie m " +
                        " JOIN movies.person p ON p.id = m.director_id " +
                        " WHERE m.id = :movieId ";

        sqlMovie = new StringBuilder(MOVIE_EXIST);
        sourceMovie.addValue("movieId", movieId, Types.INTEGER);

        // Check if user is either admin/employee or regular user account:
        try{
            List<String> roles = user.getJWTClaimsSet().getStringListClaim(JWTManager.CLAIM_ROLES);
            if (whereAdded){
                sqlMovie.append(" AND ");
            }
            else{
                sqlMovie.append(" WHERE ");
                whereAdded = true;
            }
            if (roles.size() > 0){
                if (roles.contains("ADMIN") | roles.contains("EMPLOYEE")){
                    sqlMovie.append(" (m.hidden = false OR m.hidden = true) ");
                }
                else{
                    sqlMovie.append(" m.hidden = false ");
                }
            }
            else{
                sqlMovie.append(" m.hidden = false ");
            }
        }
        catch (Exception e){
            sqlMovie.append(" m.hidden = false");
        }

        // Check database and return 1 object if it exists:
        List<MovieObjectAll> movies = this.repo.movieExist(sqlMovie.toString(), sourceMovie);

        // If our list contains nothing throw an error:
        if (movies.size() < 1){
            throw new ResultError(MoviesResults.NO_MOVIE_WITH_ID_FOUND);
        }

        // Grab our movie:
        MovieObjectAll movie = movies.get(0);

        // Begin creating queries for genre:
        StringBuilder         sqlGenre;
        MapSqlParameterSource sourceGenre     = new MapSqlParameterSource();

        final String GENRE_QUERY =
                " SELECT g.id, g.name FROM movies.genre g, " +
                        "(SELECT mg.genre_id FROM movies.movie_genre mg " +
                        " WHERE mg.movie_id = :movieId ) as genreId " +
                        " WHERE genreId.genre_id = g.id ORDER BY g.name ";

        sqlGenre = new StringBuilder(GENRE_QUERY);
        sourceGenre.addValue("movieId", movieId, Types.BIGINT);

        List<GenreObjects> genre = this.repo.movieGenre(sqlGenre.toString(), sourceGenre);

        // Begin creating queries for persons:
        StringBuilder         sqlPerson;
        MapSqlParameterSource sourcePerson     = new MapSqlParameterSource();

        final String PERSON_QUERY =
                "SELECT mp.person_id, person.name FROM movies.movie_person mp, " +
                        "(SELECT * FROM movies.person p) as person " +
                        "WHERE (mp.movie_id = :movieId AND person.id = mp.person_id) " +
                        " ORDER BY person.popularity DESC, person.id ASC ";

        sqlPerson = new StringBuilder(PERSON_QUERY);
        sourcePerson.addValue("movieId", movieId, Types.INTEGER);

        List<PersonObjects> person = this.repo.moviePerson(sqlPerson.toString(), sourcePerson);

        MovieIdSearchResponse body = new MovieIdSearchResponse()
                .setResult(MoviesResults.MOVIE_WITH_ID_FOUND)
                .setGenres(genre)
                .setPersons(person)
                .setMovies(movie);

        return ResponseEntity
                .status(body.getResult().status())
                .body(body);
    }

    @GetMapping("/person/search")
    public ResponseEntity<MoviePersonSearchResponse> moviePersonSearch(@AuthenticationPrincipal SignedJWT user,
                                                                       @RequestParam Optional<String> name,
                                                                       @RequestParam Optional<String> birthday,
                                                                       @RequestParam Optional<String> movieTitle,
                                                                       @RequestParam Optional<Integer> limit,
                                                                       @RequestParam Optional<Integer> page,
                                                                       @RequestParam Optional<String> orderBy,
                                                                       @RequestParam Optional<String> direction)
    {
        // Initialize Arguments to construct sql statement for person object:
        StringBuilder         sql;
        MapSqlParameterSource source          = new MapSqlParameterSource();
        boolean               whereAdded      = false;

        this.validate.validityCheckPerson(limit, page, orderBy, direction);

        final String MOVIE_TITLE =
            "SELECT p.id, p.name, p.birthday, p.biography, p.birthplace, p.popularity, p.profile_path FROM movies.person p " +
            "JOIN movies.movie_person mp ON mp.person_id = p.id " +
            "JOIN movies.movie m ON m.id = mp.movie_id ";

        final String NO_MOVIE_TITLE =
                "SELECT p.id, p.name, p.birthday, p.biography, p.birthplace, p.popularity, p.profile_path FROM movies.person p ";

        if (movieTitle.isPresent()){
            sql = new StringBuilder(MOVIE_TITLE);
        }
        else{
            sql = new StringBuilder(NO_MOVIE_TITLE);
        }

        if (name.isPresent()){
            if (whereAdded){
                sql.append(" AND ");
            }
            else{
                sql.append(" WHERE ");
                whereAdded = true;
            }
            sql.append( "p.name LIKE :name ");
            String wildcardSearch = '%' + name.get() + '%';
            source.addValue("name", wildcardSearch, Types.VARCHAR);
        }

        if (birthday.isPresent()){
            if (whereAdded){
                sql.append(" AND ");
            }
            else{
                sql.append(" WHERE ");
                whereAdded = true;
            }
            sql.append( "p.birthday = :birthday ");
            Date date = Date.valueOf(birthday.get());
            source.addValue("birthday", date, Types.DATE);
        }

        if (movieTitle.isPresent()){
            if (whereAdded){
                sql.append(" AND ");
            }
            else{
                sql.append(" WHERE ");
                whereAdded = true;
            }
            sql.append(" m.title LIKE :movieTitle ");
            String wildcardSearch = '%' + movieTitle.get() + '%';
            source.addValue("movieTitle", wildcardSearch, Types.VARCHAR);
        }

        // Create our order by sql statement:
        PersonOrderBy orderByString = PersonOrderBy.fromString(orderBy, direction);
        sql.append(orderByString.toSql());

        // If limit is given:
        if (limit.isPresent()){
            sql.append(" LIMIT :limit ");
            source.addValue("limit", limit.get(), Types.INTEGER);
        }

        // If limit and page are given:
        if (limit.isPresent() && page.isPresent()){
            sql.append(" OFFSET :page ");
            source.addValue("page", ((page.get() - 1) * limit.get()), Types.INTEGER);
        }

        // If limit is not given, but page is give:
        if (!limit.isPresent() && page.isPresent()){
            sql.append(" LIMIT 10 OFFSET :page ");
            source.addValue("page", ((page.get() - 1) * 10), Types.INTEGER);
        }

        // If limit and page are both not given:
        if (!limit.isPresent() && !page.isPresent()){
            sql.append(" LIMIT 10 OFFSET 0 ");
        }

        System.out.println(sql.toString());

        List<PersonObjectAll> persons = this.repo.searchPerson(sql.toString(), source);

        if (persons.size() < 1){
            throw new ResultError(MoviesResults.NO_PERSONS_FOUND_WITHIN_SEARCH);
        }

        MoviePersonSearchResponse body = new MoviePersonSearchResponse()
                .setResult(MoviesResults.PERSONS_FOUND_WITHIN_SEARCH)
                .setPersons(persons);

        return ResponseEntity
                .status(body.getResult().status())
                .body(body);
    }

    @GetMapping("person/{personId}")
    public ResponseEntity<PersonIdResponse> personIdSearch(@AuthenticationPrincipal SignedJWT user,
                                                           @PathVariable Long personId)
    {
        // Initialize Arguments to construct sql statement for movie object:
        StringBuilder         sql;
        MapSqlParameterSource source     = new MapSqlParameterSource();

        final String PERSON_EXIST =
                "SELECT p.id, p.name, p.birthday, p.biography, p.birthplace, p.popularity, p.profile_path " +
                "FROM movies.person p " +
                " WHERE p.id = :personId ";
        sql = new StringBuilder(PERSON_EXIST);
        source.addValue("personId", personId, Types.INTEGER);

        List<PersonObjectAll> persons = this.repo.searchPerson(sql.toString(), source);

        if (persons.size() < 1){
            throw new ResultError(MoviesResults.NO_PERSON_WITH_ID_FOUND);
        }

        PersonObjectAll person = persons.get(0);

        PersonIdResponse body = new PersonIdResponse()
                .setResult(MoviesResults.PERSON_WITH_ID_FOUND)
                .setPerson(person);

        return ResponseEntity
                .status(body.getResult().status())
                .body(body);
    }
}
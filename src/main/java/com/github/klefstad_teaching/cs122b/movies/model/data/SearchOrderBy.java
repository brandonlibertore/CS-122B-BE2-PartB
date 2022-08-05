package com.github.klefstad_teaching.cs122b.movies.model.data;

import java.util.Locale;
import java.util.Optional;

public enum SearchOrderBy {

    TITLE_ASC(" ORDER by m.title ASC, m.id  "),
    TITLE_DESC(" ORDER by m.title DESC, m.id  "),
    RATING_ASC(" ORDER by m.rating ASC, m.id  "),
    RATING_DESC(" ORDER by m.rating DESC, m.id  "),
    YEAR_ASC(" ORDER by m.year ASC, m.id  "),
    YEAR_DESC(" ORDER by m.year DESC, m.id  ");

    private final String sql;

    SearchOrderBy(String sql){
        this.sql = sql;
    }

    public String toSql(){
        return sql;
    }

    public static SearchOrderBy fromString(Optional<String> orderBy, Optional<String> direction){
        if (direction.isPresent()){
            if (orderBy.isPresent()){
                if (orderBy.get().equals("title")){
                    if (direction.get().toUpperCase().equals("ASC")){
                        return TITLE_ASC;
                    }
                    else{
                        return TITLE_DESC;
                    }
                }
                else if (orderBy.get().equals("rating")){
                    if (direction.get().toUpperCase().equals("ASC")){
                        return RATING_ASC;
                    }
                    else{
                        return RATING_DESC;
                    }
                }
                else{
                    if (direction.get().toUpperCase().equals("ASC")){
                        return YEAR_ASC;
                    }
                    else{
                        return YEAR_DESC;
                    }
                }
            }
            else{
                if (direction.get().toUpperCase().equals("ASC")){
                    return TITLE_ASC;
                }
                return TITLE_DESC;
            }
        }
        else{
            if (orderBy.isPresent()){
                if (orderBy.get().equals("title")){
                    return TITLE_ASC;
                }
                else if (orderBy.get().equals("rating")){
                    return RATING_ASC;
                }
                else{
                    return YEAR_ASC;
                }
            }
            else{
                return TITLE_ASC;
            }
        }
    }
}

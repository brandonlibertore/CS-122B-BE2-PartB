package com.github.klefstad_teaching.cs122b.movies.model.data;

import java.util.Optional;

public enum PersonOrderBy {
    NAME_ASC(" ORDER by p.name ASC, p.id  "),
    NAME_DESC(" ORDER by p.name DESC, p.id  "),
    POPULARITY_ASC(" ORDER by p.popularity ASC, p.id  "),
    POPULARITY_DESC(" ORDER by p.popularity DESC, p.id  "),
    BIRTHDAY_ASC(" ORDER by p.birthday ASC, p.id  "),
    BIRTHDAY_DESC(" ORDER by p.birthday DESC, p.id  ");

    private final String sql;

    PersonOrderBy(String sql){
        this.sql = sql;
    }

    public String toSql(){
        return sql;
    }

    public static PersonOrderBy fromString(Optional<String> orderBy, Optional<String> direction){
        if (direction.isPresent()){
            if (orderBy.isPresent()){
                if (orderBy.get().equals("name")){
                    if (direction.get().toUpperCase().equals("ASC")){
                        return NAME_ASC;
                    }
                    else{
                        return NAME_DESC;
                    }
                }
                else if (orderBy.get().equals("popularity")){
                    if (direction.get().toUpperCase().equals("ASC")){
                        return POPULARITY_ASC;
                    }
                    else{
                        return POPULARITY_DESC;
                    }
                }
                else{
                    if (direction.get().toUpperCase().equals("ASC")){
                        return BIRTHDAY_ASC;
                    }
                    else{
                        return BIRTHDAY_DESC;
                    }
                }
            }
            else{
                if (direction.get().toUpperCase().equals("ASC")){
                    return NAME_ASC;
                }
                return NAME_DESC;
            }
        }
        else{
            if (orderBy.isPresent()){
                if (orderBy.get().equals("name")){
                    return NAME_ASC;
                }
                else if (orderBy.get().equals("popularity")){
                    return POPULARITY_ASC;
                }
                else{
                    return BIRTHDAY_ASC;
                }
            }
            else{
                return NAME_ASC;
            }
        }
    }
}

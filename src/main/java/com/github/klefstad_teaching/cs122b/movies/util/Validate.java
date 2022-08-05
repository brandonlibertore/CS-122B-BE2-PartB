package com.github.klefstad_teaching.cs122b.movies.util;

import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Validate
{
    public void validityCheck(Optional<Integer> limit, Optional<Integer> page, Optional<String> orderBy, Optional<String> direction){

        // Check the limit if present:
        if (limit.isPresent()){
            int value = limit.get();
            if (value != 10 & value != 25 & value != 50 & value != 100){
                throw new ResultError(MoviesResults.INVALID_LIMIT);
            }
        }

        // Check the page if present:
        if (page.isPresent()){
            if (page.get() < 1){
                throw new ResultError(MoviesResults.INVALID_PAGE);
            }
        }

        // Check the orderBy if present:
        if (orderBy.isPresent()){
            if (!orderBy.get().equals("title") && !orderBy.get().equals("rating") && !orderBy.get().equals("year")){
                throw new ResultError(MoviesResults.INVALID_ORDER_BY);
            }
        }

        // Check the direction if present:
        if (direction.isPresent()){
            if (!direction.get().toUpperCase().equals("ASC") && !direction.get().toUpperCase().equals("DESC")){
                throw new ResultError(MoviesResults.INVALID_DIRECTION);
            }
        }
    }

    public void validityCheckPerson(Optional<Integer> limit, Optional<Integer> page, Optional<String> orderBy, Optional<String> direction){

        // Check the limit if present:
        if (limit.isPresent()){
            int value = limit.get();
            if (value != 10 & value != 25 & value != 50 & value != 100){
                throw new ResultError(MoviesResults.INVALID_LIMIT);
            }
        }

        // Check the page if present:
        if (page.isPresent()){
            if (page.get() < 1){
                throw new ResultError(MoviesResults.INVALID_PAGE);
            }
        }

        // Check the orderBy if present:
        if (orderBy.isPresent()){
            if (!orderBy.get().equals("name") && !orderBy.get().equals("popularity") && !orderBy.get().equals("birthday")){
                throw new ResultError(MoviesResults.INVALID_ORDER_BY);
            }
        }

        // Check the direction if present:
        if (direction.isPresent()){
            if (!direction.get().toUpperCase().equals("ASC") && !direction.get().toUpperCase().equals("DESC")){
                throw new ResultError(MoviesResults.INVALID_DIRECTION);
            }
        }
    }
}

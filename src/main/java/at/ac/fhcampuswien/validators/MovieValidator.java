package at.ac.fhcampuswien.validators;

import at.ac.fhcampuswien.models.Movie;

public class MovieValidator {

    public boolean isValid(Movie movie) {
        return movie != null
                && movie.getTitle() != null
                && !movie.getTitle().isBlank()
                && movie.getGenre() != null
                && !movie.getGenre().isBlank()
                && movie.getReleaseYear() > 0;
    }
}
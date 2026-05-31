package at.ac.fhcampuswien.services;

import at.ac.fhcampuswien.exceptions.MovieNotFoundException;
import at.ac.fhcampuswien.exceptions.DatabaseException;
import at.ac.fhcampuswien.validators.MovieValidator;

import java.util.List;
import at.ac.fhcampuswien.filters.MovieFilter;
import at.ac.fhcampuswien.models.Movie;
import at.ac.fhcampuswien.repositories.MovieRepositoryInterface;


public class MovieService {

    private MovieRepositoryInterface movieRepository;
    private final MovieValidator movieValidator = new MovieValidator();
    private final MovieFilter movieFilter = new MovieFilter();

    public MovieService(MovieRepositoryInterface movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies() throws DatabaseException {
        return movieRepository.findAll();
    }

    public boolean addMovie(Movie movie) throws DatabaseException {
        if (!movieValidator.isValid(movie)) {
            return false;
        }
        boolean exists = movieRepository.findAll().stream()
                .anyMatch(m ->
                        m.getTitle().equalsIgnoreCase(movie.getTitle()) &&
                                m.getGenre().equalsIgnoreCase(movie.getGenre()) &&
                                m.getReleaseYear() == movie.getReleaseYear()
                );

        if (exists) return false;

        movieRepository.add(movie);
        return true;
    }

    public boolean deleteMovie(String id)
            throws DatabaseException, MovieNotFoundException {

        Movie movieToDelete = movieRepository.findAll().stream()
                .filter(movie -> movie.getId().toString().equals(id))
                .findFirst()
                .orElse(null);

        if (movieToDelete == null) {
            throw new MovieNotFoundException("Movie not found");
        }

        return movieRepository.delete(movieToDelete);
    }

    public boolean updateMovie(String id, Movie updatedMovie)
            throws DatabaseException, MovieNotFoundException {
        if (!movieValidator.isValid(updatedMovie)) {
            return false;
        }
        Movie existingMovie = movieRepository.findAll().stream()
                .filter(movie -> movie.getId().toString().equals(id))
                .findFirst()
                .orElse(null);

        if (existingMovie == null) {
            throw new MovieNotFoundException("Movie not found");
        }


        existingMovie.setTitle(updatedMovie.getTitle());
        existingMovie.setGenre(updatedMovie.getGenre());
        existingMovie.setReleaseYear(updatedMovie.getReleaseYear());

        return movieRepository.update(existingMovie);
    }

    public List<Movie> searchMovies(String title, String genre, String releaseYear) {
        return movieRepository.findAll().stream()
                .filter(movie -> movieFilter.matches(movie, title, genre, releaseYear))
                .toList();
    }
}
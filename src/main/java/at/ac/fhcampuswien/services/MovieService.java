package at.ac.fhcampuswien.services;
import at.ac.fhcampuswien.repositories.MovieRepository;
import at.ac.fhcampuswien.exceptions.MovieNotFoundException;

import java.util.List;
import at.ac.fhcampuswien.models.Movie;


public class MovieService {

    private MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public boolean addMovie(Movie movie) {

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

    public boolean deleteMovie(String id) {

        Movie movieToDelete = movieRepository.findAll().stream()
                .filter(movie -> movie.getId().toString().equals(id))
                .findFirst()
                .orElse(null);

        if (movieToDelete == null) {
            throw new MovieNotFoundException("Movie not found");
        }

        return movieRepository.delete(movieToDelete);
    }
    public boolean updateMovie(String id, Movie updatedMovie) {

        Movie existingMovie = movieRepository.findAll().stream()
                .filter(movie -> movie.getId().toString().equals(id))
                .findFirst()
                .orElse(null);

        if (existingMovie == null) {
            return false;
        }

        existingMovie.setTitle(updatedMovie.getTitle());
        existingMovie.setGenre(updatedMovie.getGenre());
        existingMovie.setReleaseYear(updatedMovie.getReleaseYear());

        return movieRepository.update(existingMovie);
    }

    public List<Movie> searchMovies(String title, String genre, String releaseYear) {

        return movieRepository.findAll().stream()
                .filter(m -> title == null || m.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(m -> genre == null || m.getGenre().toLowerCase().contains(genre.toLowerCase()))
                .filter(m -> releaseYear == null ||
                        (releaseYear.matches("\\d+") && m.getReleaseYear() == Integer.parseInt(releaseYear)))
                .toList();
    }
}
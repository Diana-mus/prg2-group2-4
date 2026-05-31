package at.ac.fhcampuswien.services;
import at.ac.fhcampuswien.exceptions.MovieNotFoundException;
import at.ac.fhcampuswien.exceptions.DatabaseException;

import java.util.List;
import at.ac.fhcampuswien.models.Movie;
import at.ac.fhcampuswien.repositories.MovieRepositoryInterface;


public class MovieService {

    private MovieRepositoryInterface movieRepository;

    public MovieService(MovieRepositoryInterface movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies() throws DatabaseException {
        return movieRepository.findAll();
    }

    public boolean addMovie(Movie movie) throws DatabaseException {

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

    public List<Movie> searchMovies(String title, String genre, String releaseYear)
            throws DatabaseException {

        return movieRepository.findAll().stream()
                .filter(m -> title == null || m.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(m -> genre == null || m.getGenre().toLowerCase().contains(genre.toLowerCase()))
                .filter(m -> releaseYear == null ||
                        (releaseYear.matches("\\d+") && m.getReleaseYear() == Integer.parseInt(releaseYear)))
                .toList();
    }
}
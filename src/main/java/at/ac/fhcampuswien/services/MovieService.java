package at.ac.fhcampuswien.services;

import java.util.List;
import at.ac.fhcampuswien.models.Movie;

public class MovieService {

    private List<Movie> movies;

    public MovieService(List<Movie> movies) {
        this.movies = movies;
    }

    public List<Movie> getAllMovies() {
        return movies;
    }

    public boolean addMovie(Movie movie) {

        boolean exists = movies.stream()
                .anyMatch(m ->
                        m.getTitle().equalsIgnoreCase(movie.getTitle()) &&
                                m.getGenre().equalsIgnoreCase(movie.getGenre()) &&
                                m.getReleaseYear() == movie.getReleaseYear()
                );

        if (exists) return false;

        movies.add(movie);
        return true;
    }

    public boolean deleteMovie(String id) {
        return movies.removeIf(movie -> movie.getId().toString().equals(id));
    }

    public boolean updateMovie(String id, Movie updatedMovie) {

        return movies.stream()
                .filter(movie -> movie.getId().toString().equals(id))
                .findFirst()
                .map(movie -> {
                    movie.setTitle(updatedMovie.getTitle());
                    movie.setGenre(updatedMovie.getGenre());
                    movie.setReleaseYear(updatedMovie.getReleaseYear());
                    return true;
                })
                .orElse(false);
    }

    public List<Movie> searchMovies(String title, String genre, String releaseYear) {

        return movies.stream()
                .filter(m -> title == null || m.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(m -> genre == null || m.getGenre().toLowerCase().contains(genre.toLowerCase()))
                .filter(m -> releaseYear == null ||
                        (releaseYear.matches("\\d+") && m.getReleaseYear() == Integer.parseInt(releaseYear)))
                .toList();
    }
}
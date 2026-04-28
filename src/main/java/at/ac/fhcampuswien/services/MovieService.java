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
                .anyMatch(m -> m.getTitle().equalsIgnoreCase(movie.getTitle()));

        if (exists) {
            return false;
        }

        movies.add(movie);
        return true;
    }

    public boolean deleteMovie(String id) {
        return movies.removeIf(movie -> movie.getId().toString().equals(id));
    }

    public boolean updateMovie(String id, Movie updatedMovie) {

        for (Movie movie : movies) {
            if (movie.getId().toString().equals(id)) {

                movie.setTitle(updatedMovie.getTitle());
                movie.setGenre(updatedMovie.getGenre());
                movie.setReleaseYear(updatedMovie.getReleaseYear());

                return true;
            }
        }

        return false;
    }

    public List<Movie> searchMovies(String title, String genre, String releaseYear) {

        return movies.stream()
                .filter(movie ->
                        (title == null || movie.getTitle().toLowerCase().contains(title.toLowerCase())) &&
                                (genre == null || movie.getGenre().toLowerCase().contains(genre.toLowerCase())) &&
                                (releaseYear == null || movie.getReleaseYear() == Integer.parseInt(releaseYear))
                )
                .toList();
    }
}
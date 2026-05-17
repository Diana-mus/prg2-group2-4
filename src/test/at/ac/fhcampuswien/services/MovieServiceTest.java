package at.ac.fhcampuswien.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import at.ac.fhcampuswien.models.Movie;
import at.ac.fhcampuswien.repositories.MovieRepository;

import static org.junit.jupiter.api.Assertions.*;

public class MovieServiceTest {

    private MovieService movieService;
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {

        movieRepository = new MovieRepository();
        movieService = new MovieService(movieRepository);

        movieService.addMovie(new Movie("Inception", "Sci-Fi", 2010));
        movieService.addMovie(new Movie("Titanic", "Drama", 1997));
    }

    @Test
    void addMovie_success() {
        Movie newMovie = new Movie("Avatar", "Sci-Fi", 2009);

        boolean result = movieService.addMovie(newMovie);

        assertTrue(result);
        assertEquals(3, movieService.getAllMovies().size());
    }

    @Test
    void addMovie_duplicate() {
        Movie duplicate = new Movie("Inception", "Sci-Fi", 2010);

        boolean result = movieService.addMovie(duplicate);

        assertFalse(result);
        assertEquals(2, movieService.getAllMovies().size());
    }

    @Test
    void deleteMovie_success() {
        String id = movieService.getAllMovies().get(0).getId().toString();

        boolean result = movieService.deleteMovie(id);

        assertNotNull(result);
        assertEquals(1, movieService.getAllMovies().size());
    }

    @Test
    void deleteMovie_notFound() {
        boolean result = movieService.deleteMovie("wrong-id");

        assertFalse(result);
        assertEquals(2, movieService.getAllMovies().size());
    }

    @Test
    void updateMovie_success() {
        String id = movieService.getAllMovies().get(0).getId().toString();

        Movie updated = new Movie("Updated Title", "Drama", 2020);

        boolean result = movieService.updateMovie(id, updated);

        assertTrue(result);
        assertEquals("Updated Title", movieService.getAllMovies().get(0).getTitle());
    }

    @Test
    void updateMovie_notFound() {
        Movie updated = new Movie("Test", "Drama", 2020);

        boolean result = movieService.updateMovie("wrong-id", updated);

        assertFalse(result);
    }

    @Test
    void searchMovies_byTitle() {
        List<Movie> result = movieService.searchMovies("incep", null, null);

        assertEquals(1, result.size());
    }

    @Test
    void searchMovies_byGenre() {
        List<Movie> result = movieService.searchMovies(null, "Drama", null);

        assertEquals(1, result.size());
    }

    @Test
    void searchMovies_byYear() {
        List<Movie> result = movieService.searchMovies(null, null, "2010");

        assertEquals(1, result.size());
    }

    @Test
    void searchMovies_noResult() {
        List<Movie> result = movieService.searchMovies("xyz", null, null);

        assertTrue(result.isEmpty());
    }
}
package at.ac.fhcampuswien.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import at.ac.fhcampuswien.models.Movie;

import static org.junit.jupiter.api.Assertions.*;

public class MovieServiceTest {

    private MovieService movieService;
    private List<Movie> movies;

    @BeforeEach
    void setUp() {
        movies = new ArrayList<>();
        movies.add(new Movie("Inception", "Sci-Fi", 2010));
        movies.add(new Movie("Titanic", "Drama", 1997));

        movieService = new MovieService(movies);
    }

    // ✅ ADD MOVIE

    @Test
    void addMovie_success() {
        Movie newMovie = new Movie("Avatar", "Sci-Fi", 2009);

        boolean result = movieService.addMovie(newMovie);

        assertTrue(result);
        assertEquals(3, movies.size());
    }

    @Test
    void addMovie_duplicate() {
        Movie duplicate = new Movie("Inception", "Sci-Fi", 2010);

        boolean result = movieService.addMovie(duplicate);

        assertFalse(result);
        assertEquals(2, movies.size());
    }

    // ✅ DELETE MOVIE

    @Test
    void deleteMovie_success() {
        String id = movies.get(0).getId().toString();

        boolean result = movieService.deleteMovie(id);

        assertTrue(result);
        assertEquals(1, movies.size());
    }

    @Test
    void deleteMovie_notFound() {
        boolean result = movieService.deleteMovie("wrong-id");

        assertFalse(result);
        assertEquals(2, movies.size());
    }

    // ✅ UPDATE MOVIE

    @Test
    void updateMovie_success() {
        String id = movies.get(0).getId().toString();

        Movie updated = new Movie("Updated Title", "Drama", 2020);

        boolean result = movieService.updateMovie(id, updated);

        assertTrue(result);
        assertEquals("Updated Title", movies.get(0).getTitle());
    }

    @Test
    void updateMovie_notFound() {
        Movie updated = new Movie("Test", "Drama", 2020);

        boolean result = movieService.updateMovie("wrong-id", updated);

        assertFalse(result);
    }

    // ✅ SEARCH MOVIES

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
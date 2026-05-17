package at.ac.fhcampuswien.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import at.ac.fhcampuswien.exceptions.MovieNotFoundException;

import java.util.List;

import at.ac.fhcampuswien.models.Movie;
import at.ac.fhcampuswien.repositories.MovieRepository;

import static org.junit.jupiter.api.Assertions.*;

public class MovieServiceTest {

    private MovieService movieService;
    @Mock
    private MovieRepository movieRepository;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        movieService = new MovieService(movieRepository);

    }

    @Test
    void addMovie_success() {

        when(movieRepository.findAll()).thenReturn(List.of());

        Movie newMovie = new Movie("Avatar", "Sci-Fi", 2009);

        boolean result = movieService.addMovie(newMovie);

        assertTrue(result);

        verify(movieRepository).add(newMovie);
    }

    @Test
    void addMovie_duplicate() {

        List<Movie> movies = List.of(
                new Movie("Inception", "Sci-Fi", 2010)
        );

        when(movieRepository.findAll()).thenReturn(movies);

        Movie duplicate = new Movie("Inception", "Sci-Fi", 2010);

        boolean result = movieService.addMovie(duplicate);

        assertFalse(result);
    }


    @Test
    void deleteMovie_notFound() {

        when(movieRepository.findAll()).thenReturn(List.of());

        assertThrows(
                MovieNotFoundException.class,
                () -> movieService.deleteMovie("wrong-id")
        );
    }


    @Test
    void updateMovie_notFound() {
        Movie updated = new Movie("Test", "Drama", 2020);

        boolean result = movieService.updateMovie("wrong-id", updated);

        assertFalse(result);
    }

    @Test
    void searchMovies_byTitle() {

        List<Movie> movies = List.of(
                new Movie("Inception", "Sci-Fi", 2010)
        );

        when(movieRepository.findAll()).thenReturn(movies);

        List<Movie> result =
                movieService.searchMovies("incep", null, null);

        assertEquals(1, result.size());
    }

    @Test
    void searchMovies_byGenre() {

        List<Movie> movies = List.of(
                new Movie("Titanic", "Drama", 1997)
        );

        when(movieRepository.findAll()).thenReturn(movies);

        List<Movie> result =
                movieService.searchMovies(null, "Drama", null);

        assertEquals(1, result.size());
    }

    @Test
    void searchMovies_byYear() {

        List<Movie> movies = List.of(
                new Movie("Inception", "Sci-Fi", 2010)
        );

        when(movieRepository.findAll()).thenReturn(movies);

        List<Movie> result =
                movieService.searchMovies(null, null, "2010");

        assertEquals(1, result.size());
    }

    @Test
    void searchMovies_noResult() {
        List<Movie> result = movieService.searchMovies("xyz", null, null);

        assertTrue(result.isEmpty());
    }
}
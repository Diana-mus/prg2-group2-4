package at.ac.fhcampuswien.filters;

import at.ac.fhcampuswien.models.Movie;

public class MovieFilter {

    public boolean matches(Movie movie, String title, String genre, String releaseYear) {
        return matchesTitle(movie, title)
                && matchesGenre(movie, genre)
                && matchesReleaseYear(movie, releaseYear);
    }

    private boolean matchesTitle(Movie movie, String title) {
        return title == null || movie.getTitle().toLowerCase().contains(title.toLowerCase());
    }

    private boolean matchesGenre(Movie movie, String genre) {
        return genre == null || movie.getGenre().toLowerCase().contains(genre.toLowerCase());
    }

    private boolean matchesReleaseYear(Movie movie, String releaseYear) {
        return releaseYear == null ||
                (releaseYear.matches("\\d+") && movie.getReleaseYear() == Integer.parseInt(releaseYear));
    }
}

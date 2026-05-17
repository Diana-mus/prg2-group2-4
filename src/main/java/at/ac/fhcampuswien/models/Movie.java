package at.ac.fhcampuswien.models;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

public class Movie {
    private UUID id;
    private String title;
    private String genre;
    private int releaseYear;

    public Movie(){
        this.id = UUID.randomUUID();
    }
    public Movie(String title, String genre, int releaseYear ){
        this.id = UUID.randomUUID();
        this.title = title;
        this.genre = genre;
        this.releaseYear = releaseYear;
    }
    public Movie(UUID id, String title, String genre, int releaseYear) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.releaseYear = releaseYear;
    }
    public UUID getId(){
        return id;
    }
    public void setId(UUID id){
        this.id = id;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getGenre(){
        return genre;
    }
    public void setGenre(String genre){
        this.genre = genre;
    }
    public int getReleaseYear(){
        return releaseYear;
    }
    public void setReleaseYear(int releaseYear){
        this.releaseYear = releaseYear;
    }
    @Override
    public String toString(){
        return "Movie{ id = " + id + ",title = '" + title + "' ,genre = '" + genre + "' ,releaseYear = " + releaseYear +" }";
    }
    public static List<Movie> generateDummyMovies() {
        List<Movie> movies = new ArrayList<>();

        movies.add(new Movie("Inception", "Sci-Fi", 2010));
        movies.add(new Movie("Titanic", "Romance", 1997));
        movies.add(new Movie("The Dark Knight", "Action", 2008));
        movies.add(new Movie("Interstellar", "Sci-Fi", 2014));
        movies.add(new Movie("Avatar", "Fantasy", 2009));
        movies.add(new Movie("Gladiator", "Action", 2000));
        movies.add(new Movie("Joker", "Drama", 2019));
        movies.add(new Movie("Frozen", "Animation", 2013));
        movies.add(new Movie("Shrek", "Comedy", 2001));
        movies.add(new Movie("The Matrix", "Sci-Fi", 1999));
        movies.add(new Movie("Toy Story", "Animation", 1995));
        movies.add(new Movie("Coco", "Animation", 2017));
        movies.add(new Movie("Parasite", "Thriller", 2019));
        movies.add(new Movie("Up", "Animation", 2009));
        movies.add(new Movie("The Godfather", "Crime", 1972));
        movies.add(new Movie("Harry Potter", "Fantasy", 2001));
        movies.add(new Movie("Moana", "Animation", 2016));
        movies.add(new Movie("Black Panther", "Action", 2018));
        movies.add(new Movie("La La Land", "Musical", 2016));
        movies.add(new Movie("Dune", "Sci-Fi", 2021));

        return movies;
    }
}

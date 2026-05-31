package at.ac.fhcampuswien.repositories;

import at.ac.fhcampuswien.exceptions.DatabaseException;
import at.ac.fhcampuswien.models.Movie;

import java.util.List;



public interface MovieRepositoryInterface {
    void add(Movie movie) throws DatabaseException;

    List<Movie> findAll() throws DatabaseException;

    boolean delete(Movie movie) throws DatabaseException;

    boolean update(Movie movie) throws DatabaseException;
}

package at.ac.fhcampuswien.repositories;

import at.ac.fhcampuswien.database.DatabaseUtil;
import at.ac.fhcampuswien.exceptions.DatabaseException;
import at.ac.fhcampuswien.exceptions.MovieNotFoundException;
import at.ac.fhcampuswien.models.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class MovieRepository implements MovieRepositoryInterface {

    public void add(Movie movie) throws DatabaseException {
        String sql = "INSERT INTO movies (id, title, genre, release_year) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, movie.getId());
            statement.setString(2, movie.getTitle());
            statement.setString(3, movie.getGenre());
            statement.setInt(4, movie.getReleaseYear());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Could not add movie", e);
        }
    }

    public List<Movie> findAll() throws DatabaseException {
        String sql = "SELECT * FROM movies";
        List<Movie> movies = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Movie movie = new Movie(
                        resultSet.getObject("id", UUID.class),
                        resultSet.getString("title"),
                        resultSet.getString("genre"),
                        resultSet.getInt("release_year")
                );

                movies.add(movie);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Could not load movies", e);
        }

        return movies;
    }

    public boolean delete(Movie movie) throws DatabaseException, MovieNotFoundException {

        String sql = "DELETE FROM movies WHERE title = ? AND genre = ? AND release_year = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, movie.getTitle());
            statement.setString(2, movie.getGenre());
            statement.setInt(3, movie.getReleaseYear());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new MovieNotFoundException("Movie not found for deletion");
            }

            return true;

        } catch (SQLException e) {
            throw new DatabaseException("Could not delete movie", e);
        }
    }

    public boolean update(Movie movie)
            throws DatabaseException, MovieNotFoundException {

        String sql = "UPDATE movies SET title = ?, genre = ?, release_year = ? WHERE id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, movie.getTitle());
            statement.setString(2, movie.getGenre());
            statement.setInt(3, movie.getReleaseYear());
            statement.setObject(4, movie.getId());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new MovieNotFoundException("Movie not found for update");
            }

            return true;

        } catch (SQLException e) {
            throw new DatabaseException("Could not update movie", e);
        }
    }
}
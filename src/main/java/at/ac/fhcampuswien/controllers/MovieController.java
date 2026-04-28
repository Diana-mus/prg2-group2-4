package at.ac.fhcampuswien.controllers;

import at.ac.fhcampuswien.ApiUtils;
import at.ac.fhcampuswien.models.Movie;
import at.ac.fhcampuswien.services.MovieService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class MovieController implements HttpHandler {

    private final MovieService movieService =
            new MovieService(Movie.generateDummyMovies());

    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (path.endsWith("/getAll")) {
            handleGetAll(exchange);
        } else if (path.endsWith("/add")) {
            handleAdd(exchange);
        } else if (path.endsWith("/delete")) {
            handleDelete(exchange);
        } else if (path.endsWith("/update")) {
            handleUpdate(exchange);
        } else if (path.contains("/search")) {
            handleSearch(exchange);
        } else {
            ApiUtils.sendResponse(exchange, 404, "{ \"error\": \"Path not found\" }");
        }
    }

    private void handleGetAll(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("GET")) {
            ApiUtils.sendResponse(exchange, 405, "{ \"error\": \"Method not allowed\" }");
            return;
        }

        List<Movie> movies = movieService.getAllMovies();
        ApiUtils.sendResponse(exchange, 200, gson.toJson(movies));
    }

    private void handleAdd(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("POST")) {
            ApiUtils.sendResponse(exchange, 405, "{ \"error\": \"Method not allowed\" }");
            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Movie movie = gson.fromJson(body, Movie.class);

        boolean success = movieService.addMovie(movie);

        if (!success) {
            ApiUtils.sendResponse(exchange, 400, "{ \"error\": \"Movie already exists\" }");
            return;
        }

        ApiUtils.sendResponse(exchange, 201, gson.toJson(movie));
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("DELETE")) {
            ApiUtils.sendResponse(exchange, 405, "{ \"error\": \"Method not allowed\" }");
            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String id = extractJsonValue(body, "id");

        boolean success = movieService.deleteMovie(id);

        if (!success) {
            ApiUtils.sendResponse(exchange, 404, "{ \"error\": \"Movie not found\" }");
            return;
        }

        ApiUtils.sendResponse(exchange, 200, "{ \"message\": \"Movie deleted successfully\" }");
    }

    private void handleUpdate(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("PUT")) {
            ApiUtils.sendResponse(exchange, 405, "{ \"error\": \"Method not allowed\" }");
            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Movie updatedMovie = gson.fromJson(body, Movie.class);
        String id = extractJsonValue(body, "id");

        boolean success = movieService.updateMovie(id, updatedMovie);

        if (!success) {
            ApiUtils.sendResponse(exchange, 404, "{ \"error\": \"Movie not found\" }");
            return;
        }

        ApiUtils.sendResponse(exchange, 200, "{ \"message\": \"Movie updated successfully\" }");
    }

    private void handleSearch(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("GET")) {
            ApiUtils.sendResponse(exchange, 405, "{ \"error\": \"Method not allowed\" }");
            return;
        }

        Map<String, String> params = ApiUtils.parseQueryParams(exchange.getRequestURI().getQuery());

        List<Movie> result = movieService.searchMovies(
                params.get("title"),
                params.get("genre"),
                params.get("releaseYear")
        );

        ApiUtils.sendResponse(exchange, 200, gson.toJson(result));
    }

    private String extractJsonValue(String json, String key) {
        String search = "\"" + key + "\"";
        int keyIndex = json.indexOf(search);
        if (keyIndex == -1) return null;
        int colonIndex = json.indexOf(":", keyIndex);
        int valueStart = json.indexOf("\"", colonIndex) + 1;
        int valueEnd = json.indexOf("\"", valueStart);
        if (valueStart == 0 || valueEnd == -1) return null;
        return json.substring(valueStart, valueEnd);
    }
}
package at.ac.fhcampuswien.controllers;

import at.ac.fhcampuswien.ApiUtils;
import at.ac.fhcampuswien.models.Movie;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import com.google.gson.Gson;


public class MovieController implements HttpHandler {

    private List<Movie> movies = Movie.generateDummyMovies();
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        System.out.println("Received path: " + path);

        if (path.endsWith("/getAll")) {
            handleGetAll(exchange);
        } else if (path.endsWith("/add")) {
            handleAdd(exchange);
        } else if (path.endsWith("/delete")) {
            handleDelete(exchange);
        } else if (path.endsWith("/update")) {
            handleUpdate(exchange);
        }
        else if (path.contains("/search")) {
            handleSearch(exchange);
        } else {
            ApiUtils.sendResponse(exchange, 404, "{ \"error\": \"Path not found\" }");
        }
    }
   // GET /api/movies/search
   private void handleSearch(HttpExchange exchange) throws IOException {
       if (!exchange.getRequestMethod().equals("GET")) {
           ApiUtils.sendResponse(exchange, 405, "{ \"error\": \"Method not allowed\" }");
           return;
       }

       String query = exchange.getRequestURI().getQuery();
       Map<String, String> params = ApiUtils.parseQueryParams(query);

       String title = params.get("title");
       String genre = params.get("genre");
       String releaseYear = params.get("releaseYear");

       List<Movie> result = movies.stream()
               .filter(m -> title == null || m.getTitle().toLowerCase().contains(title.toLowerCase()))
               .filter(m -> genre == null || m.getGenre().toLowerCase().contains(genre.toLowerCase()))
               .filter(m -> releaseYear == null || String.valueOf(m.getReleaseYear()).equals(releaseYear))
               .toList();

       Gson gson = new Gson();
       String json = gson.toJson(result);

       ApiUtils.sendResponse(exchange, 200, json);
   }

    //GET /api/movies/getAll
    private void handleGetAll(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("GET")) {
            ApiUtils.sendResponse(exchange, 405, "{ \"error\": \"Method not allowed\" }");
            return;
        }
        ApiUtils.sendResponse(exchange, 200, moviesToJson(movies));
    }

    //POST /api/movies/add
    private void handleAdd(HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod().equals("POST")) {
            ApiUtils.sendResponse(exchange, 405, "{ \"error\": \"Method not allowed\" }");
            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        Movie movie = gson.fromJson(body, Movie.class);

        if (movie == null || movie.getTitle() == null || movie.getGenre() == null) {
            ApiUtils.sendResponse(exchange, 400, "{ \"error\": \"Invalid movie data\" }");
            return;
        }

        // check duplicate
        for (Movie m : movies) {
            if (m.getTitle().equals(movie.getTitle()) &&
                    m.getGenre().equals(movie.getGenre()) &&
                    m.getReleaseYear() == movie.getReleaseYear()) {

                ApiUtils.sendResponse(exchange, 400, "{ \"error\": \"Movie already exists\" }");
                return;
            }
        }

        movies.add(movie);

        String json = gson.toJson(movie);
        ApiUtils.sendResponse(exchange, 201, json);
    }

    // DELETE /api/movies/delete
    private void handleDelete(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("DELETE")) {
            ApiUtils.sendResponse(exchange, 405, "{ \"error\": \"Method not allowed\" }");
            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String title = extractJsonValue(body, "title");
        String genre = extractJsonValue(body, "genre");
        int releaseYear = extractIntValue(body, "releaseYear");

        if (title == null || genre == null || releaseYear == -1) {
            ApiUtils.sendResponse(exchange, 400, "{ \"error\": \"Invalid movie data\" }");
            return;
        }

        Movie toDelete = null;
        for (Movie m : movies) {
            if (m.getTitle().equals(title) && m.getGenre().equals(genre) && m.getReleaseYear() == releaseYear) {
                toDelete = m;
                break;
            }
        }

        if (toDelete == null) {
            ApiUtils.sendResponse(exchange, 404, "{ \"error\": \"Movie not found\" }");
            return;
        }

        movies.remove(toDelete);
        ApiUtils.sendResponse(exchange, 200, "{ \"message\": \"Movie deleted successfully\" }");
    }

    //PUT /api/movies/update
    private void handleUpdate(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("PUT")) {
            ApiUtils.sendResponse(exchange, 405, "{ \"error\": \"Method not allowed\" }");
            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String id = extractJsonValue(body, "id");
        String title = extractJsonValue(body, "title");
        String genre = extractJsonValue(body, "genre");
        int releaseYear = extractIntValue(body, "releaseYear");

        if (id == null || title == null || genre == null || releaseYear == -1) {
            ApiUtils.sendResponse(exchange, 400, "{ \"error\": \"Invalid movie data\" }");
            return;
        }

        Movie toUpdate = null;
        for (Movie m : movies) {
            if (m.getId().equals(UUID.fromString(id))) {
                toUpdate = m;
                break;
            }
        }

        if (toUpdate == null) {
            ApiUtils.sendResponse(exchange, 404, "{ \"error\": \"Movie not found\" }");
            return;
        }

        toUpdate.setTitle(title);
        toUpdate.setGenre(genre);
        toUpdate.setReleaseYear(releaseYear);
        ApiUtils.sendResponse(exchange, 200, "{ \"message\": \"Movie updated successfully\" }");
    }

    //Helper Methods

    private String movieToJson(Movie movie) {
        return "{"
                + "\"id\": \"" + movie.getId() + "\", "
                + "\"title\": \"" + movie.getTitle() + "\", "
                + "\"genre\": \"" + movie.getGenre() + "\", "
                + "\"releaseYear\": " + movie.getReleaseYear()
                + "}";
    }

    private String moviesToJson(List<Movie> movies) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < movies.size(); i++) {
            sb.append(movieToJson(movies.get(i)));
            if (i < movies.size() - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
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

    private int extractIntValue(String json, String key) {
        String search = "\"" + key + "\"";
        int keyIndex = json.indexOf(search);
        if (keyIndex == -1) return -1;
        int colonIndex = json.indexOf(":", keyIndex) + 1;
        StringBuilder number = new StringBuilder();
        for (int i = colonIndex; i < json.length(); i++) {
            char c = json.charAt(i);
            if (Character.isDigit(c)) number.append(c);
            else if (!number.isEmpty()) break;
        }
        if (number.isEmpty()) return -1;
        return Integer.parseInt(number.toString());
    }
}

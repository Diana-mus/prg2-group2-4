package at.ac.fhcampuswien.factory;

public class ErrorResponseFactory {

    public static String error(String message) {
        return "{ \"error\": \"" + message + "\" }";
    }

    public static String success(String message) {
        return "{ \"message\": \"" + message + "\" }";
    }
}
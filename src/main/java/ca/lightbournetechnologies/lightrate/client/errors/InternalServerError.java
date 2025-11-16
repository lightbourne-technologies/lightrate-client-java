package ca.lightbournetechnologies.lightrate.client.errors;

/**
 * 500 Internal Server Error errors.
 */
public class InternalServerError extends APIError {
    public InternalServerError(String message, Integer statusCode, String responseBody) {
        super(message, statusCode, responseBody);
    }
}

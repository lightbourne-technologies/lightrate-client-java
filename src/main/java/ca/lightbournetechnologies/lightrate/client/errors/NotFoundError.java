package ca.lightbournetechnologies.lightrate.client.errors;

/**
 * 404 Not Found errors.
 */
public class NotFoundError extends APIError {
    public NotFoundError(String message, Integer statusCode, String responseBody) {
        super(message, statusCode, responseBody);
    }
}

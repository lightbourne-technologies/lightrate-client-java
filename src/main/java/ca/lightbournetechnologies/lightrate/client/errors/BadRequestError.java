package ca.lightbournetechnologies.lightrate.client.errors;

/**
 * 400 Bad Request errors.
 */
public class BadRequestError extends APIError {
    public BadRequestError(String message, Integer statusCode, String responseBody) {
        super(message, statusCode, responseBody);
    }
}

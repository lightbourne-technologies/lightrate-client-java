package ca.lightbournetechnologies.lightrate.client.errors;

/**
 * 401 Unauthorized errors.
 */
public class UnauthorizedError extends APIError {
    public UnauthorizedError(String message, Integer statusCode, String responseBody) {
        super(message, statusCode, responseBody);
    }
}

package ca.lightbournetechnologies.lightrate.client.errors;

/**
 * 403 Forbidden errors.
 */
public class ForbiddenError extends APIError {
    public ForbiddenError(String message, Integer statusCode, String responseBody) {
        super(message, statusCode, responseBody);
    }
}

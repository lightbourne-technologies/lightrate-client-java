package ca.lightbournetechnologies.lightrate.client.errors;

/**
 * 429 Too Many Requests errors.
 */
public class TooManyRequestsError extends APIError {
    public TooManyRequestsError(String message, Integer statusCode, String responseBody) {
        super(message, statusCode, responseBody);
    }
}

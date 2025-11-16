package ca.lightbournetechnologies.lightrate.client.errors;

/**
 * 422 Unprocessable Entity errors.
 */
public class UnprocessableEntityError extends APIError {
    public UnprocessableEntityError(String message, Integer statusCode, String responseBody) {
        super(message, statusCode, responseBody);
    }
}

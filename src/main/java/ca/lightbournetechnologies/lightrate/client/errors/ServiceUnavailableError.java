package ca.lightbournetechnologies.lightrate.client.errors;

/**
 * 503 Service Unavailable errors.
 */
public class ServiceUnavailableError extends APIError {
    public ServiceUnavailableError(String message, Integer statusCode, String responseBody) {
        super(message, statusCode, responseBody);
    }
}

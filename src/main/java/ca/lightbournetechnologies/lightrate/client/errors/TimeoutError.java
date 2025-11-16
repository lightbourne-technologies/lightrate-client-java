package ca.lightbournetechnologies.lightrate.client.errors;

/**
 * Request timeout errors.
 */
public class TimeoutError extends LightRateError {
    public TimeoutError(String message) {
        super(message);
    }
}

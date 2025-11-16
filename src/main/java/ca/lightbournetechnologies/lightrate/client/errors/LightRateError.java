package ca.lightbournetechnologies.lightrate.client.errors;

/**
 * Base error class for all LightRate errors.
 */
public class LightRateError extends RuntimeException {
    public LightRateError(String message) {
        super(message);
    }

    public LightRateError(String message, Throwable cause) {
        super(message, cause);
    }
}

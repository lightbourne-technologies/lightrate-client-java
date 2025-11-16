package ca.lightbournetechnologies.lightrate.client.errors;

/**
 * Network-related errors.
 */
public class NetworkError extends LightRateError {
    public NetworkError(String message) {
        super(message);
    }

    public NetworkError(String message, Throwable cause) {
        super(message, cause);
    }
}

package ca.lightbournetechnologies.lightrate.client.errors;

/**
 * Base API error class.
 */
public class APIError extends LightRateError {
    private final Integer statusCode;
    private final String responseBody;

    public APIError(String message, Integer statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}

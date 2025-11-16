package ca.lightbournetechnologies.lightrate.client.errors;

/**
 * Factory class for creating errors from HTTP responses.
 */
public class Errors {
    /**
     * Factory function to create appropriate error based on HTTP status code.
     */
    public static APIError createErrorFromResponse(String message, int statusCode, String responseBody) {
        switch (statusCode) {
            case 400:
                return new BadRequestError(message, statusCode, responseBody);
            case 401:
                return new UnauthorizedError(message, statusCode, responseBody);
            case 403:
                return new ForbiddenError(message, statusCode, responseBody);
            case 404:
                return new NotFoundError(message, statusCode, responseBody);
            case 422:
                return new UnprocessableEntityError(message, statusCode, responseBody);
            case 429:
                return new TooManyRequestsError(message, statusCode, responseBody);
            case 500:
                return new InternalServerError(message, statusCode, responseBody);
            case 503:
                return new ServiceUnavailableError(message, statusCode, responseBody);
            default:
                return new APIError(message, statusCode, responseBody);
        }
    }
}

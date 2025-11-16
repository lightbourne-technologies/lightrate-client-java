# LightRate Client Java

A Java client for the Lightrate token management API, providing easy-to-use methods for consuming tokens with local bucket management.

## Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>ca.lightbournetechnologies</groupId>
    <artifactId>lightrate-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

Or for Gradle:

```gradle
implementation 'ca.lightbournetechnologies:lightrate-client:1.0.0'
```

## Usage

### Basic Usage

```java
import ca.lightbournetechnologies.lightrate.client.LightRateClient;
import ca.lightbournetechnologies.lightrate.client.models.ClientOptions;
import ca.lightbournetechnologies.lightrate.client.models.ConsumeTokensResponse;

// Simple usage - pass your API key and application ID
LightRateClient client = new LightRateClient("your_api_key", "your_application_id");

// With additional options
ClientOptions options = new ClientOptions();
options.setTimeout(60);
options.setDefaultLocalBucketSize(10);
LightRateClient client = new LightRateClient("your_api_key", "your_application_id", options);
```

### Consuming Tokens

```java
// Consume tokens by operation
ConsumeTokensResponse response = client.consumeTokens(
    "user123",      // userIdentifier
    1,              // tokensRequested
    "send_email",   // operation
    null,           // path
    null            // httpMethod
);

// Or consume tokens by path
ConsumeTokensResponse response = client.consumeTokens(
    "user123",              // userIdentifier
    1,                      // tokensRequested
    null,                   // operation
    "/api/v1/emails/send",  // path
    "POST"                  // httpMethod
);

if (response.getTokensConsumed() > 0) {
    System.out.println("Tokens consumed successfully. Remaining: " + response.getTokensRemaining());
} else {
    System.out.println("Failed to consume tokens");
}
```

#### Using Local Token Buckets

The client supports local token buckets for improved performance. Buckets are automatically created based on the rules returned by the API, and are matched against incoming requests using the `matcher` field from the rule.

```java
// Configure client with default bucket size
ClientOptions options = new ClientOptions();
options.setDefaultLocalBucketSize(20);  // All operations use this bucket size
LightRateClient client = new LightRateClient("your_api_key", "your_application_id", options);

// Consume tokens using local bucket (more efficient)
ConsumeLocalBucketTokenResponse result = client.consumeLocalBucketToken(
    "user123",      // userIdentifier
    "send_email",   // operation
    null,           // path
    null            // httpMethod
);

System.out.println("Success: " + result.isSuccess());
System.out.println("Used local token: " + result.isUsedLocalToken());
System.out.println("Bucket status: " + result.getBucketStatus());
```

**Bucket Matching:**
- Buckets are matched using the `matcher` field from the rule, which supports regex patterns
- Each user has separate buckets per rule, ensuring proper isolation
- Buckets expire after 60 seconds of inactivity
- Default rules (isDefault: true) do not create local buckets

### Complete Example

```java
import ca.lightbournetechnologies.lightrate.client.LightRateClient;
import ca.lightbournetechnologies.lightrate.client.errors.*;

// Create a client with your API key and application ID
LightRateClient client = new LightRateClient("your_api_key", "your_application_id");

try {
    // Consume tokens
    ConsumeTokensResponse consumeResponse = client.consumeTokens(
        "user123",
        1,
        "send_email",
        null,
        null
    );

    if (consumeResponse.getTokensConsumed() > 0) {
        System.out.println("Successfully consumed tokens. Remaining: " + 
                          consumeResponse.getTokensRemaining());
        // Proceed with your operation
    } else {
        System.out.println("Failed to consume tokens");
        // Handle rate limiting
    }

} catch (UnauthorizedError e) {
    System.out.println("Authentication failed: " + e.getMessage());
} catch (TooManyRequestsError e) {
    System.out.println("Rate limited: " + e.getMessage());
} catch (APIError e) {
    System.out.println("API Error (" + e.getStatusCode() + "): " + e.getMessage());
} catch (NetworkError e) {
    System.out.println("Network error: " + e.getMessage());
}
```

## Error Handling

The client provides comprehensive error handling with specific exception types:

```java
try {
    ConsumeTokensResponse response = client.consumeTokens(...);
} catch (UnauthorizedError e) {
    System.out.println("Authentication failed: " + e.getMessage());
} catch (NotFoundError e) {
    System.out.println("Resource not found: " + e.getMessage());
} catch (APIError e) {
    System.out.println("API Error (" + e.getStatusCode() + "): " + e.getMessage());
} catch (NetworkError e) {
    System.out.println("Network error: " + e.getMessage());
} catch (TimeoutError e) {
    System.out.println("Request timed out: " + e.getMessage());
}
```

Available error types:
- `LightRateError` - Base error class
- `ConfigurationError` - Configuration-related errors
- `APIError` - Base API error class
- `BadRequestError` - 400 errors
- `UnauthorizedError` - 401 errors
- `ForbiddenError` - 403 errors
- `NotFoundError` - 404 errors
- `UnprocessableEntityError` - 422 errors
- `TooManyRequestsError` - 429 errors
- `InternalServerError` - 500 errors
- `ServiceUnavailableError` - 503 errors
- `NetworkError` - Network-related errors
- `TimeoutError` - Request timeout errors

## API Reference

### Classes

#### `LightRateClient`

Main client class for interacting with the LightRate API.

**Constructor:**
```java
LightRateClient(String apiKey, String applicationId)
LightRateClient(String apiKey, String applicationId, ClientOptions options)
```

**Methods:**

- `consumeTokens(userIdentifier, tokensRequested, operation, path, httpMethod) -> ConsumeTokensResponse`
- `consumeLocalBucketToken(userIdentifier, operation, path, httpMethod) -> ConsumeLocalBucketTokenResponse`
- `consumeTokensWithRequest(request) -> ConsumeTokensResponse`
- `getAllBucketStatuses() -> Map<String, TokenBucketStatus>`
- `resetAllBuckets() -> void`
- `getConfiguration() -> Configuration`

## Development

After checking out the repo, run `mvn clean install` to build and install the package locally.

## Contributing

Bug reports and pull requests are welcome on GitHub at https://github.com/lightbourne-technologies/lightrate-client-java. This project is intended to be a safe, welcoming space for collaboration, and contributors are expected to adhere to the code of conduct.

## License

The package is available as open source under the terms of the [MIT License](https://opensource.org/licenses/MIT).

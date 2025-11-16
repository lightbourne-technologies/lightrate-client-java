package ca.lightbournetechnologies.lightrate.client;

import ca.lightbournetechnologies.lightrate.client.config.Configuration;
import ca.lightbournetechnologies.lightrate.client.errors.*;
import ca.lightbournetechnologies.lightrate.client.models.*;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Main client class for interacting with the LightRate API.
 */
public class LightRateClient {
    private static final String VERSION = "1.0.0";
    private static final String BASE_URL = "https://api.lightrate.lightbournetechnologies.ca";
    
    private final Configuration configuration;
    private final OkHttpClient httpClient;
    private final Gson gson;
    private final Map<String, TokenBucket> tokenBuckets;
    private final ReentrantLock bucketsLock;

    /**
     * Initialize the client.
     *
     * @param apiKey API key for authentication
     * @param applicationId Application ID
     * @param options Optional client options
     */
    public LightRateClient(String apiKey, String applicationId, ClientOptions options) {
        if (apiKey != null && applicationId != null) {
            this.configuration = new Configuration();
            this.configuration.setApiKey(apiKey);
            this.configuration.setApplicationId(applicationId);
            if (options != null) {
                if (options.getTimeout() != null) {
                    this.configuration.setTimeout(options.getTimeout());
                }
                if (options.getRetryAttempts() != null) {
                    this.configuration.setRetryAttempts(options.getRetryAttempts());
                }
                if (options.getDefaultLocalBucketSize() != null) {
                    this.configuration.setDefaultLocalBucketSize(options.getDefaultLocalBucketSize());
                }
            }
        } else {
            this.configuration = new Configuration();
        }

        this.tokenBuckets = new ConcurrentHashMap<>();
        this.bucketsLock = new ReentrantLock();
        this.gson = new Gson();
        
        validateConfiguration();
        this.httpClient = setupHttpClient();
    }

    /**
     * Initialize the client with default options.
     *
     * @param apiKey API key for authentication
     * @param applicationId Application ID
     */
    public LightRateClient(String apiKey, String applicationId) {
        this(apiKey, applicationId, null);
    }

    /**
     * Consume tokens by operation or path using local bucket.
     *
     * @param userIdentifier User identifier
     * @param operation Optional operation name
     * @param path Optional path
     * @param httpMethod Optional HTTP method (required when path is provided)
     * @return Response indicating success and bucket status
     */
    public ConsumeLocalBucketTokenResponse consumeLocalBucketToken(
            String userIdentifier,
            String operation,
            String path,
            String httpMethod) {
        // First, try to find an existing bucket that matches this request
        TokenBucket bucket = findBucketByMatcher(userIdentifier, operation, path, httpMethod);

        if (bucket != null) {
            boolean consumed = bucket.checkAndConsumeToken();
            if (consumed) {
                return new ConsumeLocalBucketTokenResponse(
                        true,
                        true,
                        bucket.getStatus()
                );
            }
        }

        // Still empty, make API call
        int tokensToFetch = configuration.getDefaultLocalBucketSize();
        ConsumeTokensRequest request = new ConsumeTokensRequest();
        request.setOperation(operation);
        request.setPath(path);
        request.setHttpMethod(httpMethod);
        request.setUserIdentifier(userIdentifier);
        request.setTokensRequested(tokensToFetch);
        request.setTokensRequestedForDefaultBucketMatch(1);
        request.setApplicationId(configuration.getApplicationId());

        ConsumeTokensResponse response = consumeTokensWithRequest(request);

        if (response.getRule() != null && response.getRule().isDefault()) {
            return new ConsumeLocalBucketTokenResponse(
                    response.getTokensConsumed() > 0,
                    false,
                    null
            );
        }

        if (response.getRule() != null) {
            TokenBucket newBucket = fillBucketAndCreateIfNotExists(
                    userIdentifier,
                    response.getRule(),
                    response.getTokensConsumed()
            );

            boolean newBucketTokensAvailable = newBucket.checkAndConsumeToken();

            return new ConsumeLocalBucketTokenResponse(
                    newBucketTokensAvailable,
                    false,
                    newBucket.getStatus()
            );
        }

        return new ConsumeLocalBucketTokenResponse(false, false, null);
    }

    /**
     * Consume tokens directly from API.
     *
     * @param userIdentifier User identifier
     * @param tokensRequested Number of tokens to consume
     * @param operation Optional operation name
     * @param path Optional path
     * @param httpMethod Optional HTTP method (required when path is provided)
     * @return Response with token consumption details
     */
    public ConsumeTokensResponse consumeTokens(
            String userIdentifier,
            int tokensRequested,
            String operation,
            String path,
            String httpMethod) {
        ConsumeTokensRequest request = new ConsumeTokensRequest();
        request.setOperation(operation);
        request.setPath(path);
        request.setHttpMethod(httpMethod);
        request.setUserIdentifier(userIdentifier);
        request.setTokensRequested(tokensRequested);
        request.setTokensRequestedForDefaultBucketMatch(1);
        request.setApplicationId(configuration.getApplicationId());

        return consumeTokensWithRequest(request);
    }

    /**
     * Consume tokens using a request object.
     *
     * @param request Token consumption request
     * @return Response with token consumption details
     */
    public ConsumeTokensResponse consumeTokensWithRequest(ConsumeTokensRequest request) {
        if (!isValidConsumeTokensRequest(request)) {
            throw new IllegalArgumentException("Invalid request: validation failed");
        }

        String responseBody = post("/api/v1/tokens/consume", gson.toJson(request));
        return parseConsumeTokensResponse(responseBody);
    }

    /**
     * Get all bucket statuses.
     *
     * @return Map of bucket statuses
     */
    public Map<String, TokenBucketStatus> getAllBucketStatuses() {
        Map<String, TokenBucketStatus> statuses = new HashMap<>();
        for (Map.Entry<String, TokenBucket> entry : tokenBuckets.entrySet()) {
            statuses.put(entry.getKey(), entry.getValue().getStatus());
        }
        return statuses;
    }

    /**
     * Reset all token buckets.
     */
    public void resetAllBuckets() {
        tokenBuckets.clear();
    }

    /**
     * Get configuration.
     *
     * @return Configuration object
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    private TokenBucket findBucketByMatcher(
            String userIdentifier,
            String operation,
            String path,
            String httpMethod) {
        for (TokenBucket bucket : tokenBuckets.values()) {
            if (bucket.matches(operation, path, httpMethod) &&
                    bucket.getUserIdentifier().equals(userIdentifier)) {
                return bucket;
            }
        }
        return null;
    }

    private TokenBucket fillBucketAndCreateIfNotExists(
            String userIdentifier,
            Rule rule,
            int tokenCount) {
        String bucketKey = userIdentifier + ":rule:" + rule.getId();

        bucketsLock.lock();
        try {
            TokenBucket bucket = tokenBuckets.get(bucketKey);
            if (bucket == null || bucket.expired()) {
                bucket = new TokenBucket(
                        configuration.getDefaultLocalBucketSize(),
                        rule.getId(),
                        userIdentifier,
                        rule.getMatcher(),
                        rule.getHttpMethod()
                );
                tokenBuckets.put(bucketKey, bucket);
            }

            bucket.refill(tokenCount);
            return bucket;
        } finally {
            bucketsLock.unlock();
        }
    }

    private void validateConfiguration() {
        if (!configuration.isValid()) {
            throw new ConfigurationError("API key and application ID are required");
        }
    }

    private OkHttpClient setupHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(configuration.getTimeout(), java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(configuration.getTimeout(), java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(configuration.getTimeout(), java.util.concurrent.TimeUnit.SECONDS);

        return builder.build();
    }

    private String post(String path, String jsonBody) {
        String url = BASE_URL + path;
        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "Bearer " + configuration.getApiKey())
                .addHeader("User-Agent", "lightrate-client-java/" + VERSION)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Application-Id", configuration.getApplicationId())
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            return handleResponse(response);
        } catch (IOException e) {
            throw new NetworkError("Network error: " + e.getMessage());
        }
    }

    private String handleResponse(Response response) throws IOException {
        int statusCode = response.code();
        String responseBody = response.body() != null ? response.body().string() : "";

        if (statusCode >= 200 && statusCode < 300) {
            return responseBody;
        }

        String errorMessage = "Unknown error";
        try {
            Map<String, Object> errorData = gson.fromJson(responseBody, Map.class);
            if (errorData != null) {
                if (errorData.containsKey("message")) {
                    errorMessage = (String) errorData.get("message");
                } else if (errorData.containsKey("error")) {
                    errorMessage = (String) errorData.get("error");
                } else {
                    errorMessage = "HTTP " + statusCode + " Error";
                }
            }
        } catch (Exception e) {
            errorMessage = "HTTP " + statusCode + " Error";
        }

        throw Errors.createErrorFromResponse(errorMessage, statusCode, responseBody);
    }

    private boolean isValidConsumeTokensRequest(ConsumeTokensRequest request) {
        if (request.getUserIdentifier() == null || request.getUserIdentifier().trim().isEmpty()) {
            return false;
        }
        if (request.getTokensRequested() == null || request.getTokensRequested() <= 0) {
            return false;
        }
        if (request.getOperation() == null && request.getPath() == null) {
            return false;
        }
        if (request.getOperation() != null && request.getPath() != null) {
            return false;
        }
        if (request.getPath() != null && request.getHttpMethod() == null) {
            return false;
        }
        return true;
    }

    private ConsumeTokensResponse parseConsumeTokensResponse(String json) {
        return gson.fromJson(json, ConsumeTokensResponse.class);
    }
}

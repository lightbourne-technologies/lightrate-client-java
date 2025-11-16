package ca.lightbournetechnologies.lightrate.client.config;

/**
 * Configuration class for the LightRate client.
 */
public class Configuration {
    private String apiKey;
    private String applicationId;
    private int timeout = 30;
    private int retryAttempts = 3;
    private int defaultLocalBucketSize = 5;

    /**
     * Check if the configuration is valid.
     *
     * @return true if valid
     */
    public boolean isValid() {
        return apiKey != null && !apiKey.isEmpty() &&
               applicationId != null && !applicationId.isEmpty();
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getRetryAttempts() {
        return retryAttempts;
    }

    public void setRetryAttempts(int retryAttempts) {
        this.retryAttempts = retryAttempts;
    }

    public int getDefaultLocalBucketSize() {
        return defaultLocalBucketSize;
    }

    public void setDefaultLocalBucketSize(int defaultLocalBucketSize) {
        this.defaultLocalBucketSize = defaultLocalBucketSize;
    }
}

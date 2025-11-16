package ca.lightbournetechnologies.lightrate.client.models;

/**
 * Options for creating a client.
 */
public class ClientOptions {
    private Integer timeout;
    private Integer retryAttempts;
    private Integer defaultLocalBucketSize;

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getRetryAttempts() {
        return retryAttempts;
    }

    public void setRetryAttempts(Integer retryAttempts) {
        this.retryAttempts = retryAttempts;
    }

    public Integer getDefaultLocalBucketSize() {
        return defaultLocalBucketSize;
    }

    public void setDefaultLocalBucketSize(Integer defaultLocalBucketSize) {
        this.defaultLocalBucketSize = defaultLocalBucketSize;
    }
}

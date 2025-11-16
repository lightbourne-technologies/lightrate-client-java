package ca.lightbournetechnologies.lightrate.client.models;

/**
 * Response from local bucket token consumption.
 */
public class ConsumeLocalBucketTokenResponse {
    private boolean success;
    private boolean usedLocalToken;
    private TokenBucketStatus bucketStatus;

    public ConsumeLocalBucketTokenResponse(boolean success, boolean usedLocalToken, TokenBucketStatus bucketStatus) {
        this.success = success;
        this.usedLocalToken = usedLocalToken;
        this.bucketStatus = bucketStatus;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isUsedLocalToken() {
        return usedLocalToken;
    }

    public void setUsedLocalToken(boolean usedLocalToken) {
        this.usedLocalToken = usedLocalToken;
    }

    public TokenBucketStatus getBucketStatus() {
        return bucketStatus;
    }

    public void setBucketStatus(TokenBucketStatus bucketStatus) {
        this.bucketStatus = bucketStatus;
    }
}

package ca.lightbournetechnologies.lightrate.client.models;

/**
 * Request object for consuming tokens.
 */
public class ConsumeTokensRequest {
    private String operation;
    private String path;
    private String httpMethod;
    private String userIdentifier;
    private Integer tokensRequested;
    private Integer tokensRequestedForDefaultBucketMatch;
    private String applicationId;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public Integer getTokensRequested() {
        return tokensRequested;
    }

    public void setTokensRequested(Integer tokensRequested) {
        this.tokensRequested = tokensRequested;
    }

    public Integer getTokensRequestedForDefaultBucketMatch() {
        return tokensRequestedForDefaultBucketMatch;
    }

    public void setTokensRequestedForDefaultBucketMatch(Integer tokensRequestedForDefaultBucketMatch) {
        this.tokensRequestedForDefaultBucketMatch = tokensRequestedForDefaultBucketMatch;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
}

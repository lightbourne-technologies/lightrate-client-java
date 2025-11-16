package ca.lightbournetechnologies.lightrate.client.models;

/**
 * Status of a token bucket.
 */
public class TokenBucketStatus {
    private int tokensRemaining;
    private int maxTokens;

    public TokenBucketStatus(int tokensRemaining, int maxTokens) {
        this.tokensRemaining = tokensRemaining;
        this.maxTokens = maxTokens;
    }

    public int getTokensRemaining() {
        return tokensRemaining;
    }

    public void setTokensRemaining(int tokensRemaining) {
        this.tokensRemaining = tokensRemaining;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }
}

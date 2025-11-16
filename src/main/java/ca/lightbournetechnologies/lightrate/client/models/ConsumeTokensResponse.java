package ca.lightbournetechnologies.lightrate.client.models;

/**
 * Response from token consumption API call.
 */
public class ConsumeTokensResponse {
    private int tokensRemaining;
    private int tokensConsumed;
    private int throttles;
    private Rule rule;

    public int getTokensRemaining() {
        return tokensRemaining;
    }

    public void setTokensRemaining(int tokensRemaining) {
        this.tokensRemaining = tokensRemaining;
    }

    public int getTokensConsumed() {
        return tokensConsumed;
    }

    public void setTokensConsumed(int tokensConsumed) {
        this.tokensConsumed = tokensConsumed;
    }

    public int getThrottles() {
        return throttles;
    }

    public void setThrottles(int throttles) {
        this.throttles = throttles;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }
}

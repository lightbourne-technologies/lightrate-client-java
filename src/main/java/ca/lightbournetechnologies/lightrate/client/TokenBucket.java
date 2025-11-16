package ca.lightbournetechnologies.lightrate.client;

import ca.lightbournetechnologies.lightrate.client.models.TokenBucketStatus;

import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Token bucket for local token management.
 */
public class TokenBucket {
    private int availableTokens;
    private final int maxTokens;
    private final String ruleId;
    private final String matcher;
    private final String httpMethod;
    private final String userIdentifier;
    private Date lastAccessedAt;
    private final ReentrantLock lock;

    public TokenBucket(
            int maxTokens,
            String ruleId,
            String userIdentifier,
            String matcher,
            String httpMethod) {
        this.maxTokens = maxTokens;
        this.availableTokens = 0;
        this.ruleId = ruleId;
        this.matcher = matcher;
        this.httpMethod = httpMethod;
        this.userIdentifier = userIdentifier;
        this.lastAccessedAt = new Date();
        this.lock = new ReentrantLock();
    }

    public boolean hasTokens() {
        return availableTokens > 0;
    }

    public boolean consumeToken() {
        if (availableTokens <= 0) {
            return false;
        }
        availableTokens -= 1;
        return true;
    }

    public int consumeTokens(int count) {
        if (count <= 0 || availableTokens <= 0) {
            return 0;
        }
        int tokensToConsume = Math.min(count, availableTokens);
        availableTokens -= tokensToConsume;
        return tokensToConsume;
    }

    public int refill(int tokensToFetch) {
        touch();
        int tokensToAdd = Math.min(tokensToFetch, maxTokens - availableTokens);
        availableTokens += tokensToAdd;
        return tokensToAdd;
    }

    public TokenBucketStatus getStatus() {
        return new TokenBucketStatus(availableTokens, maxTokens);
    }

    public void reset() {
        availableTokens = 0;
    }

    public boolean matches(String operation, String path, String httpMethod) {
        if (expired()) {
            return false;
        }

        if (matcher == null) {
            return false;
        }

        try {
            Pattern matcherPattern = Pattern.compile(matcher);

            // For operation-based requests, match against operation
            if (operation != null) {
                return matcherPattern.matcher(operation).find() && this.httpMethod == null;
            }

            // For path-based requests, match against path and HTTP method
            if (path != null) {
                return matcherPattern.matcher(path).find() &&
                       (this.httpMethod == null || this.httpMethod.equals(httpMethod));
            }

            return false;
        } catch (PatternSyntaxException e) {
            // If matcher is not a valid regex, fall back to exact match
            if (operation != null) {
                return matcher.equals(operation) && this.httpMethod == null;
            } else if (path != null) {
                return matcher.equals(path) &&
                       (this.httpMethod == null || this.httpMethod.equals(httpMethod));
            }
            return false;
        }
    }

    public boolean expired() {
        Date now = new Date();
        long diffMs = now.getTime() - lastAccessedAt.getTime();
        return diffMs > 60000; // 60 seconds
    }

    public void touch() {
        lastAccessedAt = new Date();
    }

    public boolean checkAndConsumeToken() {
        lock.lock();
        try {
            touch();
            if (availableTokens > 0) {
                availableTokens -= 1;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }
}

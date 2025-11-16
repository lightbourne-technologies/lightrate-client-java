package ca.lightbournetechnologies.lightrate.client.models;

/**
 * Rate limiting rule.
 */
public class Rule {
    private String id;
    private String name;
    private int refillRate;
    private int burstRate;
    private boolean isDefault;
    private String matcher;
    private String httpMethod;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRefillRate() {
        return refillRate;
    }

    public void setRefillRate(int refillRate) {
        this.refillRate = refillRate;
    }

    public int getBurstRate() {
        return burstRate;
    }

    public void setBurstRate(int burstRate) {
        this.burstRate = burstRate;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getMatcher() {
        return matcher;
    }

    public void setMatcher(String matcher) {
        this.matcher = matcher;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }
}

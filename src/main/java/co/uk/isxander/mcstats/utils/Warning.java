package co.uk.isxander.mcstats.utils;

public class Warning {

    private int id;
    private String message;
    private long issueTime;
    private long lifetime;
    private boolean expired;

    public Warning(int id, String message, long lifeTime) {
        this.id = id;
        this.message = message;
        this.issueTime = System.currentTimeMillis();
        this.lifetime = lifeTime;
        this.expired = false;
    }

    public final void update() {
        // Expired
        if (lifetime != -1 && System.currentTimeMillis() - issueTime >= lifetime) {
            expired = true;
            onExpired();
        }

        onUpdate();
    }

    protected void onUpdate() {

    }

    protected void onExpired() {

    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(long issueTime) {
        this.issueTime = issueTime;
    }

    public long getLifetime() {
        return lifetime;
    }

    public void setLifetime(long lifetime) {
        this.lifetime = lifetime;
    }

    public boolean hasExpired() {
        return expired;
    }
}

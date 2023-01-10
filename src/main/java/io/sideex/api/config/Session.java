package io.sideex.api.config;

public class Session {
    private String sessionId = "";
    private boolean keepSessionAlive = false;

    public void setKeepSessionAlive(boolean keepSessionAlive) {
        this.keepSessionAlive = keepSessionAlive;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public boolean getKeepSessionAlive() {
        return keepSessionAlive;
    }
}

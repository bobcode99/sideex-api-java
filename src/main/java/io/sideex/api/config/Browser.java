package io.sideex.api.config;

import java.util.Collections;
import java.util.Map;

public class Browser {
    private final boolean active = true;
    private boolean keepSessionAlive = false;
    private Map<String, Object> capbility = Collections.emptyMap();

    public void setCapbility(Map<String, Object> capbility) {
        this.capbility = capbility;
    }

    public void setKeepSessionAlive(boolean keepSessionAlive) {
        this.keepSessionAlive = keepSessionAlive;
    }

    public Map<String, Object> getCapbility() {
        return capbility;
    }

    public boolean getKeepSessionAlive() {
        return keepSessionAlive;
    }
}

package io.sideex.api.config;

public class Play {
    private int mode = 2;
    private String entry = "";
    private int autoWaitTimeout = 30;
    private int speed = 5;
    private Period period = new Period();

    public void setAutoWaitTimeout(int autoWaitTimeout) {
        this.autoWaitTimeout = autoWaitTimeout;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public void setMode(int mode) {
        if (mode >= 0 || mode <= 2)
            this.mode = mode;
        else {
            throw new Error("Unknown mode value " + mode);
        }
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public void setSpeed(int speed) {
        if (speed >= 0 || speed <= 5)
            this.speed = speed;
        else {
            throw new Error("Unknown speed value " + speed);
        }
    }

    public int getAutoWaitTimeout() {
        return autoWaitTimeout;
    }

    public String getEntry() {
        return entry;
    }

    public int getMode() {
        return mode;
    }

    public Period getPeriod() {
        return period;
    }

    public int getSpeed() {
        return speed;
    }
}

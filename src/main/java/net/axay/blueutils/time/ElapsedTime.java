package net.axay.blueutils.time;

public class ElapsedTime {

    private long elapsedTime;
    private Double elapsedTimeSeconds;

    public ElapsedTime(long startTime, long stopTime) {
        this(stopTime - startTime);
    }

    public ElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public long getNanoTime() {
        return elapsedTime;
    }

    public double getSecondsTime() {
        if (elapsedTimeSeconds == null) {
            elapsedTimeSeconds = Timer.secondsTimeFromNanoTime(elapsedTime);
        }
        return elapsedTimeSeconds;
    }

    @Override
    public String toString() {
        return String.valueOf(elapsedTime);
    }

    public String toSecondsString() {
        return Timer.secondsStringFromSecondsTime(getSecondsTime());
    }

    public String toSecondsString(int maxFractionDigits) {
        return Timer.secondsStringFromSecondsTime(getSecondsTime(), maxFractionDigits);
    }

}
package net.axay.blueutils.time;

public class ElapsedTime {

    private long elapsedTime;
    private double elapsedTimeSeconds;

    public ElapsedTime(long startTime, long stopTime) {
        this(stopTime - startTime);
    }

    public ElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
        this.elapsedTimeSeconds = Timer.secondsTimeFromNanoTime(elapsedTime);
    }

    public long getNanoTime() {
        return elapsedTime;
    }

    public double getSecondsTime() {
        return elapsedTimeSeconds;
    }

    @Override
    public String toString() {
        return String.valueOf(elapsedTime);
    }

    public String toSecondsString() {
        return Timer.secondsStringFromSecondsTime(elapsedTimeSeconds);
    }

    public String toSecondsString(int maxFractionDigits) {
        return Timer.secondsStringFromSecondsTime(elapsedTimeSeconds, maxFractionDigits);
    }

}
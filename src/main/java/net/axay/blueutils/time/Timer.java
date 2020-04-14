package net.axay.blueutils.time;

import java.text.DecimalFormat;

public class Timer {

    private long startTime;
    private long stopTime;

    public void start() {
        startTime = System.nanoTime();
    }

    public void stop() {
        stopTime = System.nanoTime();
    }

    public ElapsedTime getElapsedTime() {
        return new ElapsedTime(startTime, stopTime);
    }

    public static double secondsTimeFromNanoTime(long nanoTime) {
        return (double) nanoTime / 1_000_000_000.0;
    }

    public static String secondsStringFromSecondsTime(double secondsTime) {
        return secondsStringFromSecondsTime(secondsTime, 20);
    }

    public static String secondsStringFromSecondsTime(double secondsTime, int maxFractionDigits) {
        DecimalFormat secondsFormat = new DecimalFormat("#.#");
        secondsFormat.setMaximumFractionDigits(maxFractionDigits);
        return secondsFormat.format(secondsTime);
    }

}
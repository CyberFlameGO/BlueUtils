package net.axay.blueutils.maths;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

    public static int randomIntInRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

}